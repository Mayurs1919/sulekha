package Sulekhaai.WHBRD.controller;

import Sulekhaai.WHBRD.model.Camera;
import Sulekhaai.WHBRD.services.CameraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(
        origins = {"http://localhost:3000/", "http://localhost:3001", "http://192.168.1.121:5173","https://sulekha-aii.netlify.app", "http://103.35.134.173"},
        allowCredentials = "true")
@RestController
@RequestMapping("/api")
public class CameraController {

    @Autowired
    private CameraService cameraService;

    @GetMapping("/cameras/connected/{userId}")
    public ResponseEntity<?> getConnectedCameras(@PathVariable String userId) {
        Long uid;
        try {
            uid = Long.parseLong(userId);
        } catch (NumberFormatException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false,
                            "message", "Invalid userId, must be numeric"));
        }

        List<Camera> cams = cameraService.getCamerasByUserId(uid);

        // Return the custom cameraId instead of internal DB id
        List<Map<String, Object>> devices = cams.stream().map(cam -> {
            Map<String, Object> map = new HashMap<>();
            map.put("cameraId", cam.getCameraId()); // ✅ return user-defined cameraId
            map.put("deviceName", cam.getDeviceName());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(Map.of("devices", devices));
    }

    @GetMapping("/cameras/logs/{userId}")
    public ResponseEntity<?> getCameraLogs(@PathVariable String userId) {
        Long uid;
        try {
            uid = Long.parseLong(userId);
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Invalid userId"));
        }

        return ResponseEntity.ok(Map.of("logs", cameraService.getLogs(uid)));
    }

    @DeleteMapping("/cameras/logs/{userId}")
    public ResponseEntity<?> clearLogs(@PathVariable String userId) {
        Long uid;
        try {
            uid = Long.parseLong(userId);
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Invalid userId"));
        }

        boolean cleared = cameraService.clearLogs(uid);
        return ResponseEntity.ok(Map.of("success", cleared));
    }

    @GetMapping("/cameras/{cameraId}/status")
    public ResponseEntity<?> getCameraStatus(@PathVariable String cameraId) {
        Long cid;
        try {
            cid = Long.parseLong(cameraId);
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Invalid cameraId"));
        }

        boolean online = cameraService.isCameraAvailable(cid);
        return ResponseEntity.ok(Map.of("available", online));
    }

    @PostMapping("/cameras/link")
    public ResponseEntity<?> linkCamera(@RequestBody Map<String, Object> req) {
        boolean ok = cameraService.linkCameraToUser(req);
        return ResponseEntity.ok(Map.of("success", ok,
                "message", ok ? "Camera linked" : "Link failed"));
    }

    @PostMapping("/api/disconnect_camera")
    public ResponseEntity<Map<String, Object>> disconnectCamera(@RequestBody Map<String, Object> req) {
        boolean result = cameraService.disconnectCamera(req);
        if (result) {
            return ResponseEntity.ok(Map.of("success", true, "message", "Camera disconnected"));
        } else {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "Failed to disconnect"));
        }
    }

}
