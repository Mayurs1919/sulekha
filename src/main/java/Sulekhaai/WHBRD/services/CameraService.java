package Sulekhaai.WHBRD.services;

import Sulekhaai.WHBRD.model.Camera;
import Sulekhaai.WHBRD.model.CameraLog;
import Sulekhaai.WHBRD.repository.CameraRepository;
import Sulekhaai.WHBRD.repository.CameraLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CameraService {

    @Autowired
    private CameraRepository cameraRepo;

    @Autowired
    private CameraLogRepository logRepo;

    /**
     * Get all cameras linked to a specific user.
     */
    public List<Camera> getCamerasByUserId(Long userId) {
        return cameraRepo.findByUserId(userId);
    }

    /**
     * Get all logs for a specific user.
     */
    public List<CameraLog> getLogs(Long userId) {
        return logRepo.findByUserId(userId);
    }

    /**
     * Delete all logs for a specific user.
     */
    public boolean clearLogs(Long userId) {
        try {
            logRepo.deleteByUserId(userId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();  // Helps identify any DB issues
            return false;
        }
    }

    /**
     * Check if a specific camera (by DB ID) is marked active.
     */
    public boolean isCameraAvailable(Long cameraId) {
        return cameraRepo.findById(cameraId)
                .map(Camera::isActive)
                .orElse(false);
    }

    /**
     * Link a new camera to a user, using custom cameraId (string).
     */
    public boolean linkCameraToUser(Map<String, Object> req) {
        try {
            Long userId = Long.parseLong(req.get("userId").toString());
            String deviceName = req.get("deviceName").toString();
            String cameraId = req.get("cameraId").toString();  // ✅ Ensure this field exists in request

            Camera cam = new Camera();
            cam.setUserId(userId);
            cam.setDeviceName(deviceName);
            cam.setCameraId(cameraId);  // ✅ Save cameraId as string (e.g., "CAM_123")
            cam.setActive(true);
            cam.setOnline(true);

            cameraRepo.save(cam);
            return true;
        } catch (Exception e) {
            e.printStackTrace();  // ✅ Useful for debugging issues like missing keys
            return false;
        }
    }

    /**
     * ✅ Disconnect a camera by custom cameraId (String).
     */
    public boolean disconnectCamera(Map<String, Object> req) {
        try {
            String cameraId = req.get("camera_id").toString();  // 🔄 Use string-based ID from request

            cameraRepo.findByCameraId(cameraId).ifPresent(cam -> {
                cam.setActive(false);
                cam.setOnline(false);
                cameraRepo.save(cam);
            });

            return true;
        } catch (Exception e) {
            e.printStackTrace();  // ✅ Helpful in case of nulls or DB issues
            return false;
        }
    }
}
