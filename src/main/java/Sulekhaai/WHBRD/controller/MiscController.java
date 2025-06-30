package Sulekhaai.WHBRD.controller;

import Sulekhaai.WHBRD.model.UserSettings;
import Sulekhaai.WHBRD.services.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")  // Base path for all below endpoints
public class MiscController {

    @Autowired
    private SettingsService service;

    /**
     * Returns timezone list with dummy entry (expand as needed).
     * Example: GET /api/utils/timezones
     */
    @GetMapping("/utils/timezones")
    public List<Map<String, String>> timezones() {
        return List.of(
            Map.of(
                "name", "Asia/Kolkata",
                "country", "IN",
                "localTime", LocalTime.now().toString()
            )
        );
    }

    /**
     * Logs out user (dummy for now).
     * Example: POST /api/auth/logout
     */
    @PostMapping("/auth/logout")
    public Map<String, String> logout() {
        // Token blacklist/invalidation logic (if needed)
        return Map.of("message", "Logged out successfully");
    }

    /**
     * Downloads the settings profile as JSON.
     * Example: GET /api/user/download-settings?email=user@example.com
     */
    @GetMapping(value = "/user/download-settings", produces = "application/json")
    public UserSettings download(@RequestParam String email) {
        return service.getProfile(email);
    }
}
