package Sulekhaai.WHBRD.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class GoogleConfig {

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier() {
        return new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance()  // ✅ Replaced deprecated JacksonFactory
        )
        .setAudience(Collections.singletonList("13015010561-ljac6odfbd7en7qq89s7a14m8gvi1a9n.apps.googleusercontent.com"))
        .build();
    }
}
