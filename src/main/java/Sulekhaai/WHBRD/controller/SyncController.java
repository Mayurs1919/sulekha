// SyncController.java
package Sulekhaai.WHBRD.controller;

import Sulekhaai.WHBRD.model.SyncStatus;
import Sulekhaai.WHBRD.services.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class SyncController {

    @Autowired private SettingsService service;

    @PostMapping("/sync/manual")
    public Map<String, Object> manual(@RequestParam String email) {
        SyncStatus s = service.manualSync(email);
        return Map.of("status", s.getStatus(),
                      "syncedAt", s.getLastSynced(),
                      "message", "Sync completed");
    }

    @GetMapping("/sync/status")
    public SyncStatus status(@RequestParam String email) {
        return service.getSyncStatus(email);
    }
}
