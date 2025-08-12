package Sulekhaai.WHBRD.config;



import Sulekhaai.WHBRD.services.JwtFilter;

import Sulekhaai.WHBRD.services.CustomUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;

import org.springframework.web.cors.CorsConfigurationSource;

import org.springframework.web.cors.UrlBasedCorsConfigurationSource;



import java.util.List;



@Configuration

public class SecurityConfig {



    @Autowired

    private CustomUserDetailsService customUserDetailsService;



    @Bean

    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {

        http

            .csrf(csrf -> csrf.disable())

            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth

                // Public endpoints

                .requestMatchers("/", "/index.html", "/error").permitAll()

                .requestMatchers("/auth/**", "/test-connection", "/send-otp", "/verify-otp").permitAll()

                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()



                // Stream endpoints

                .requestMatchers("/stream/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/proxy/stream").permitAll()

                .requestMatchers(HttpMethod.POST, "/api/proxy/stream").permitAll()



                // Device actions

                .requestMatchers("/api/disconnect_camera").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")



                // Authenticated APIs

                .requestMatchers("/api/cameras/connected/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                .requestMatchers(

                    "/dashboard/**",

                    "/cameras/**",

                    "/cameras/get-camera-list",

                    "/camera/**",

                    "/link-device",

                    "/preview/latest/**",

                    "/get_connected_devices/**",

                    "/api/logs/**"

                ).hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")




                // Task endpoints


                .requestMatchers(HttpMethod.POST, "/api/tasks", "/api/tasks/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")


                .requestMatchers(HttpMethod.PUT, "/api/tasks/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")


                .requestMatchers(HttpMethod.DELETE, "/api/tasks/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")




                // Catch-all

                .anyRequest().authenticated()

            );



        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }



    @Bean

    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(

            // Local dev

            "http://localhost:5173",

            "http://localhost:3001",

            "http://localhost:3000",

            "http://127.0.0.1:5173",

            "http://127.0.0.1:3000",



            // Production

            "https://sulekha-ai.netlify.app",

            "https://sulekha-w89v.onrender.com",

            "https://sulekha-aii.netlify.app"

        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        config.setAllowedHeaders(List.of("*", "Authorization", "Content-Type", "Accept", "X-Requested-With", "Origin"));

        config.setExposedHeaders(List.of("Authorization"));

        config.setAllowCredentials(true);

        config.setMaxAge(3600L);



        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;

    }



    @Bean

    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }



    @Bean

    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {

        return cfg.getAuthenticationManager();

    }



    @Bean

    public UserDetailsService userDetailsService() {

        return customUserDetailsService;

    }

}