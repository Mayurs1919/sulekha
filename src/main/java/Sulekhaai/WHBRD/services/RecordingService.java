package Sulekhaai.WHBRD.services;

import Sulekhaai.WHBRD.model.*;
import Sulekhaai.WHBRD.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecordingService {
    @Autowired
    private RecordingSessionRepository sessionRepo;
    @Autowired
    private SessionImageRepository imageRepo;
    @Autowired
    private UserRepository userRepo;

    // Start a new session
    public RecordingSession startSession(Long userId, String cameraId) {
        UserEntity user = userRepo.findById(userId).orElseThrow();
        RecordingSession session = new RecordingSession();
        session.setUser(user);
        session.setCameraId(cameraId);
        session.setStartTime(LocalDateTime.now());
        return sessionRepo.save(session);
    }

    // Save image for a session
    public SessionImage saveImage(Long sessionId, byte[] imageBytes) throws IOException {
        RecordingSession session = sessionRepo.findById(sessionId).orElseThrow();
        String dir = "recordings/session_" + sessionId;
        new File(dir).mkdirs();
        String filename = "img_" + System.currentTimeMillis() + "_" + UUID.randomUUID() + ".jpg";
        String path = dir + "/" + filename;
        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(imageBytes);
        }
        SessionImage img = new SessionImage();
        img.setSession(session);
        img.setImagePath(path);
        img.setTimestamp(LocalDateTime.now());
        return imageRepo.save(img);
    }

    // End session and generate video
    public void endSession(Long sessionId) throws IOException, InterruptedException {
        RecordingSession session = sessionRepo.findById(sessionId).orElseThrow();
        session.setEndTime(LocalDateTime.now());
        // Generate video from images
        String dir = "recordings/session_" + sessionId;
        String videoPath = dir + "/session_" + sessionId + ".mp4";
        // ffmpeg command: ffmpeg -framerate 2 -pattern_type glob -i 'img_*.jpg' -c:v libx264 -pix_fmt yuv420p out.mp4
        ProcessBuilder pb = new ProcessBuilder(
            "ffmpeg", "-framerate", "2", "-pattern_type", "glob", "-i", dir + "/img_*.jpg",
            "-c:v", "libx264", "-pix_fmt", "yuv420p", videoPath
        );
        pb.redirectErrorStream(true);
        Process proc = pb.start();
        proc.waitFor();
        session.setVideoPath(videoPath);
        sessionRepo.save(session);
    }

    public List<RecordingSession> getSessionsForUser(Long userId) {
        return sessionRepo.findByUserId(userId);
    }

    public Optional<RecordingSession> getSession(Long sessionId) {
        return sessionRepo.findById(sessionId);
    }
} 