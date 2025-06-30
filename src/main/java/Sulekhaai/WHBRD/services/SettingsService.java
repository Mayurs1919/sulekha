package Sulekhaai.WHBRD.services;

import Sulekhaai.WHBRD.model.*;
import Sulekhaai.WHBRD.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class SettingsService {

    @Autowired private UserSettingsRepo settingsRepo;
    @Autowired private SyncStatusRepo   syncRepo;

    public UserSettings getProfile(String email) {
        return settingsRepo.findById(email).orElse(null);
    }

    public void saveProfile(UserSettings settings) {
        settingsRepo.save(settings);
    }

    public SyncStatus manualSync(String email) {
        SyncStatus s = new SyncStatus();
        s.setEmail(email);
        s.setStatus("success");
        s.setLastSynced(LocalDateTime.now());
        syncRepo.save(s);
        return s;
    }

    public SyncStatus getSyncStatus(String email) {
        return syncRepo.findById(email)
                       .orElseGet(() -> {
                           SyncStatus s = new SyncStatus();
                           s.setEmail(email);
                           s.setStatus("Idle");
                           return s;
                       });
    }
}
