package Sulekhaai.WHBRD.controller;

import Sulekhaai.WHBRD.model.UserActionLog;
import Sulekhaai.WHBRD.repository.UserActionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/logs")
public class UserActionLogController {

    @Autowired
    private UserActionLogRepository logRepo;

    // ✅ Explicit GET endpoint to avoid ambiguity
    @GetMapping("/all")
    public List<UserActionLog> getAllLogs() {
        return logRepo.findAll();
    }

    // ✅ Explicit POST endpoint to avoid ambiguity
    @PostMapping("/save")
    public UserActionLog logAction(@RequestBody Map<String, String> payload, Principal principal) {
        UserActionLog log = new UserActionLog();

        // Allow userId directly or from the logged-in principal
        if (payload.containsKey("userId")) {
            try {
                log.setUserId(Long.parseLong(payload.get("userId")));
            } catch (Exception ignored) {}
        } else if (principal != null) {
            try {
                log.setUserId(Long.parseLong(principal.getName()));
            } catch (Exception ignored) {}
        }

        log.setAction(payload.getOrDefault("action", payload.getOrDefault("details", "unknown")));
        log.setDetails(payload.getOrDefault("details", payload.getOrDefault("action", "")));
        log.setTimestamp(LocalDateTime.now());

        return logRepo.save(log);
    }

