package Sulekhaai.WHBRD.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    /* ------------------------------------------------------------------
     * HS512 requires ≥64-byte key.  EXACTLY THE SAME key must be used
     * everywhere tokens are generated or verified.
     * ------------------------------------------------------------------ */
    private static final String SECRET_KEY =
        "Fw8vUYtBjP9zULvXgE1zRbLWmJ3sMfU9eFdhZk3CHt9wSYvQfKs6qXgqpjv7NTAE"; // 64 chars → 512 bits

    /* Token lifetime (24 h) */
    private static final long EXPIRATION_MILLIS = 1000L * 60 * 60 * 24;

    /* ------------------------------------------------------------------
     * Internal helpers
     * ------------------------------------------------------------------ */
    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    private JwtParser parser() {
        return Jwts.parserBuilder().setSigningKey(getKey()).build();
    }

    /* ------------------------------------------------------------------
     * Generate a token
     * ------------------------------------------------------------------ */
    public String generateToken(String email, Long userId, String role) {
        return Jwts.builder()
                   .setSubject(email)
                   .claim("userId", userId)
                   .claim("role",  role)
                   .setIssuedAt(new Date())
                   .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MILLIS))
                   .signWith(getKey(), SignatureAlgorithm.HS512)
                   .compact();
    }

    /* ------------------------------------------------------------------
     * Validate & extract helpers
     * ------------------------------------------------------------------ */
    public boolean validateToken(String token) {
        try {
            parser().parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public boolean validateToken(String token, UserDetails ud) {
        return extractEmail(token).equals(ud.getUsername()) && validateToken(token);
    }

    public String extractEmail(String t)      { return extractClaim(t, Claims::getSubject); }
    public Long   extractUserId(String t)     { return claim(t,"userId", Long.class); }
    public String extractUserRole(String t)   { return claim(t,"role",   String.class); }
    public Date   extractExpiration(String t) { return extractClaim(t, Claims::getExpiration); }

    // ✅ Alias extractUsername() to extractEmail()
    public String extractUsername(String token) { return extractEmail(token); }

    /* generic claim helpers */
    private <T> T claim(String t, String k, Class<T> c) { return extractAll(t).get(k, c); }
    private <T> T extractClaim(String t, Function<Claims,T> r){ return r.apply(extractAll(t)); }
    private Claims extractAll(String t){ return parser().parseClaimsJws(t).getBody(); }
    private boolean isTokenExpired(String t){ return extractExpiration(t).before(new Date()); }
}
