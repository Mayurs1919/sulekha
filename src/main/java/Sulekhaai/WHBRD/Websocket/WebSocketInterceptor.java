package Sulekhaai.WHBRD.Websocket;

import Sulekhaai.WHBRD.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class WebSocketInterceptor implements HandshakeInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            @NonNull Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpReq = servletRequest.getServletRequest();
            String token = httpReq.getParameter("token");

            if (token != null && jwtUtil.validateToken(token)) {
                try {
                    Long userId = jwtUtil.extractUserId(token);
                    String role = jwtUtil.extractUserRole(token);
                    String email = jwtUtil.extractEmail(token);

                    attributes.put("userId", userId);
                    attributes.put("role", role);
                    attributes.put("email", email); // Optional, if you need it later

                    return true;
                } catch (Exception e) {
                    System.out.println("Token validation error: " + e.getMessage());
                }
            }
        }

        response.setStatusCode(HttpStatus.FORBIDDEN);
        return false;
    }

    @Override
    public void afterHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            @Nullable Exception exception) {
        System.out.println("[WebSocket Interceptor] Handshake completed");
    }
}
