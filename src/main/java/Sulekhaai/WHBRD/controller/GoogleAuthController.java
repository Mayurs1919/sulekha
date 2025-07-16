package Sulekhaai.WHBRD.controller;

import Sulekhaai.WHBRD.model.UserEntity;
import Sulekhaai.WHBRD.repository.UserRepository;
import Sulekhaai.WHBRD.util.JwtUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class GoogleAuthController {

    @Autowired
    private GoogleIdTokenVerifier verifier;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> body) {
        try {
            String idTokenString = body.get("token");
            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                UserEntity user = userRepo.findByEmail(email)
                        .orElseGet(() -> {
                            UserEntity newUser = new UserEntity();
                            newUser.setEmail(email);
                            newUser.setName(name);
                            newUser.setRole("ROLE_USER");
                            return userRepo.save(newUser);
                        });

                String jwt = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole());

                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "jwt", jwt,
                        "user", user
                ));
            } else {
                return ResponseEntity.status(401).body(Map.of(
                        "success", false,
                        "message", "Invalid ID token"
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Internal server error"
            ));
        }
    }
}
