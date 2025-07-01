package Sulekhaai.WHBRD.Websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private WebSocketInterceptor webSocketInterceptor;

    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/preview")  // ✅ Matching /ws/preview/cameraId?token=...
                .addInterceptors(webSocketInterceptor)
                .setAllowedOrigins(
                    "http://localhost:5173",
                    "http://192.168.1.63:5173",
                    "http://192.168.1.63:5174"
                );
        // ❌ Do not use .withSockJS() if frontend is native WebSocket
    }

    @Override
    public void configureMessageBroker(@NonNull MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");              // ✅ Backend to frontend topic path
        config.setApplicationDestinationPrefixes("/app"); // ✅ Frontend to backend messages (optional)
    }
}