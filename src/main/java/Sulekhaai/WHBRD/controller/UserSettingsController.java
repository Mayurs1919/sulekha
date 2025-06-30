// UserSettingsController.java
package Sulekhaai.WHBRD.controller;

import Sulekhaai.WHBRD.model.UserSettings;
import Sulekhaai.WHBRD.services.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")

public class UserSettingsController {

    @Autowired private SettingsService service;

    @GetMapping("/profile")
    public UserSettings profile(@RequestParam String email) {
        return service.getProfile(email);
    }

    @PostMapping("/profile/update")
    public Map<String, String> update(@RequestBody UserSettings settings) {
        service.saveProfile(settings);
        return Map.of("message", "Profile updated successfully");
    }
}
