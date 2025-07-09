package Sulekhaai.WHBRD.controller;

import Sulekhaai.WHBRD.model.UserSettings;
import Sulekhaai.WHBRD.repository.UserSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin(origins = "*")
public class SettingsController {

    @Autowired
    private UserSettingsRepository settingsRepo;

    @PostMapping("/save")
    public ResponseEntity<?> saveSettings(@RequestBody UserSettings settings) {
        Optional<UserSettings> existing = settingsRepo.findByEmail(settings.getEmail());
        if (existing.isPresent()) {
            settings.setId(existing.get().getId()); // preserve same ID
        }
        UserSettings saved = settingsRepo.save(settings);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getSettings(@PathVariable String email) {
        return settingsRepo.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
