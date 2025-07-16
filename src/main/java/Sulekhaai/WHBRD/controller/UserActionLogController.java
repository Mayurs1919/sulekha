package Sulekhaai.WHBRD.controller;

import Sulekhaai.WHBRD.model.UserActionLog;
import Sulekhaai.WHBRD.repository.UserActionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/logs")
public class UserActionLogController {
    @Autowired
    private UserActionLogRepository logRepo;

    @GetMapping
    public List<UserActionLog> getAllLogs() {
        return logRepo.findAll();
    }

    @PostMapping
    public UserActionLog logAction(@RequestBody Map<String, String> payload, Principal principal) {
        UserActionLog log = new UserActionLog();
        // Accept both userId/details or username/action for backward compatibility
        if (payload.containsKey("userId")) {
            try { log.setUserId(Long.parseLong(payload.get("userId"))); } catch (Exception ignored) {}
        } else if (principal != null) {
            try { log.setUserId(Long.parseLong(principal.getName())); } catch (Exception ignored) {}
        }
        log.setAction(payload.getOrDefault("action", payload.getOrDefault("details", "unknown")));
        log.setDetails(payload.getOrDefault("details", payload.getOrDefault("action", "")));
        log.setTimestamp(java.time.LocalDateTime.now());
        return logRepo.save(log);
    }
} 