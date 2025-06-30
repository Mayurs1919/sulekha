package Sulekhaai.WHBRD.controller;

import Sulekhaai.WHBRD.model.LogEntry;
import Sulekhaai.WHBRD.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    @Autowired
    private LogRepository logRepo;

    @PostMapping
    public LogEntry createLog(@RequestBody LogEntry log) {
        log.setTimestamp(LocalDateTime.now());
        return logRepo.save(log);
    }

    @GetMapping
    public List<LogEntry> getAllLogs() {
        return logRepo.findAll();
    }
}