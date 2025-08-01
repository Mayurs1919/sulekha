package Sulekhaai.WHBRD.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.util.Utils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class GoogleConfig {

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier() {
        return new GoogleIdTokenVerifier.Builder(
                Utils.getDefaultTransport(),
                Utils.getDefaultJsonFactory()
        )
        .setAudience(Collections.singletonList(
                "370380922779-7eu8og3sgec1qflnmf8sqkg5d7fblq36.apps.googleusercontent.com"
        ))
        .build();
    }
}
