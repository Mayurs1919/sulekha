package Sulekhaai.WHBRD.controller;

import Sulekhaai.WHBRD.model.UserEntity;
import Sulekhaai.WHBRD.repository.UserRepository;
import Sulekhaai.WHBRD.services.EmailService;
import Sulekhaai.WHBRD.services.OtpService;
import Sulekhaai.WHBRD.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:3001",
        "http://192.168.1.121:8081",
        "http://103.35.134.173",
        "http://192.168.1.121:5173",
        "https://sulekha-aii.netlify.app"
})
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    // ---------------------- SIGNUP ---------------------- //
    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody Map<String, String> req) {
        String email = req.get("email");
        String password = req.get("password");
        String name = req.get("name");

        if (email == null || password == null || name == null ||
                email.isBlank() || password.isBlank() || name.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Email, name, and password are required"
            ));
        }

        if (userRepo.findByEmail(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "success", false,
                    "message", "User already exists"
            ));
        }

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setName(name);
        user.setRole("ROLE_USER");
        user.setCameras(new HashSet<>());
        userRepo.save(user);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "User registered"
        ));
    }

    // ---------------------- LOGIN ---------------------- //
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> req) {
        String email = req.get("email");
        String password = req.get("password");

        if (email == null || password == null ||
                email.isBlank() || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Email and password are required"
            ));
        }

        Optional<UserEntity> userOpt = userRepo.findByEmail(email);
        if (userOpt.isPresent() && encoder.matches(password, userOpt.get().getPassword())) {
            UserEntity user = userOpt.get();
            String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole());
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "token", token,
                    "role", user.getRole(),
                    "userId", user.getId()
            ));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                "success", false,
                "message", "Invalid credentials"
        ));
    }

    // ---------------------- SEND OTP ---------------------- //
    @PostMapping(value = "/send-otp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> sendOtp(@RequestBody Map<String, String> req) {
        String email = req.get("email");

        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Email is required"
            ));
        }

        try {
            String otp = otpService.generateOtp(email);
            System.out.println("[DEBUG] Generated OTP for " + email + ": " + otp);

            emailService.sendOtpEmail(email, otp); // Will throw if mail fails
            System.out.println("[DEBUG] OTP email sent successfully to " + email);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "OTP sent to " + email
            ));
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to send OTP to " + email + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Failed to send OTP. Please try again."
            ));
        }
    }

    // ---------------------- VERIFY OTP ---------------------- //
    @PostMapping(value = "/verify-otp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody(required = false) Map<String, String> req) {
        System.out.println("[DEBUG] /auth/verify-otp called");

        if (req == null || !req.containsKey("email") || !req.containsKey("otp")) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Email and OTP are required in request body"
            ));
        }

        String email = req.get("email");
        String otp = req.get("otp");

        if (email == null || otp == null || email.isBlank() || otp.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Email and OTP must not be blank"
            ));
        }

        boolean isValid = otpService.verifyOtp(email, otp);
        if (isValid) {
            System.out.println("[DEBUG] OTP verified successfully for: " + email);
            Optional<UserEntity> userOpt = userRepo.findByEmail(email);

            if (userOpt.isPresent()) {
                String token = jwtUtil.generateToken(email, userOpt.get().getId(), userOpt.get().getRole());

                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "OTP verified successfully",
                        "token", token,
                        "role", userOpt.get().getRole(),
                        "userId", userOpt.get().getId()
                ));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "User not found"
                ));
            }
        } else {
            System.out.println("[DEBUG] OTP verification failed for: " + email);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", "Invalid or expired OTP"
            ));
        }
    }
}
