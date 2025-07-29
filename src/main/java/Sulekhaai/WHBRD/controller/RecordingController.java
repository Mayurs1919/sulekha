package Sulekhaai.WHBRD.controller;

import Sulekhaai.WHBRD.model.RecordingSession;
import Sulekhaai.WHBRD.services.RecordingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recordings")
public class RecordingController {
    @Autowired
    private RecordingService recordingService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<RecordingSession>> getRecordingsForUser(@PathVariable Long userId) {
        List<RecordingSession> sessions = recordingService.getSessionsForUser(userId);
        return ResponseEntity.ok(sessions);
    }
}