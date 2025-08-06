package Sulekhaai.WHBRD.controller;

import Sulekhaai.WHBRD.model.UserSettings;
import Sulekhaai.WHBRD.repository.UserSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin(origins = "*")
public class SettingsController {

    @Autowired
    private UserSettingsRepository settingsRepo;

    @PostMapping("/save")
    public ResponseEntity<?> saveSettings(@RequestBody UserSettings settings) {
        try {
            // Validate input
            if (settings.getEmail() == null || settings.getEmail().isBlank()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Email is required"
                ));
            }

            // Check if user is authenticated and matches the email
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", "Authentication required"
                ));
            }

            String authenticatedEmail = auth.getName();
            if (!authenticatedEmail.equals(settings.getEmail())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                    "success", false,
                    "message", "You can only update your own settings"
                ));
            }

            // Save or update settings
        Optional<UserSettings> existing = settingsRepo.findByEmail(settings.getEmail());
        if (existing.isPresent()) {
            settings.setId(existing.get().getId()); // preserve same ID
        }
            
        UserSettings saved = settingsRepo.save(settings);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Settings saved successfully",
                "data", saved
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Failed to save settings: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getSettings(@PathVariable String email) {
        try {
            // Check if user is authenticated and matches the email
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", "Authentication required"
                ));
            }

            String authenticatedEmail = auth.getName();
            if (!authenticatedEmail.equals(email)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                    "success", false,
                    "message", "You can only access your own settings"
                ));
            }

            Optional<UserSettings> settings = settingsRepo.findByEmail(email);
            if (settings.isPresent()) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", settings.get()
                ));
            } else {
                // Return empty settings object if not found
                UserSettings emptySettings = new UserSettings();
                emptySettings.setEmail(email);
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", emptySettings
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Failed to retrieve settings: " + e.getMessage()
            ));
        }
    }
}
