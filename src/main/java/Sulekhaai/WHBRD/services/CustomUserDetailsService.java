package Sulekhaai.WHBRD.services;

import Sulekhaai.WHBRD.model.UserEntity;
import Sulekhaai.WHBRD.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    /**
     * Load user by email (used during login)
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return buildUserDetails(user);
    }

    /**
     * Load user by ID (used by JwtFilter)
     */
    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        UserEntity user = userRepo.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + id));

        return buildUserDetails(user);
    }

    /**
     * Helper to create Spring Security User with role-based authority
     */
    private UserDetails buildUserDetails(UserEntity user) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole())) // e.g. ROLE_USER or ROLE_ADMIN
        );
    }
}
