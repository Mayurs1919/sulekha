package Sulekhaai.WHBRD.services;

import Sulekhaai.WHBRD.model.Camera;
import Sulekhaai.WHBRD.model.CameraLog;
import Sulekhaai.WHBRD.model.UserEntity;
import Sulekhaai.WHBRD.repository.CameraRepository;
import Sulekhaai.WHBRD.repository.CameraLogRepository;
import Sulekhaai.WHBRD.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CameraService {

    @Autowired
    private CameraRepository cameraRepo;

    @Autowired
    private CameraLogRepository logRepo;

    @Autowired
    private UserRepository userRepo;

    /**
     * Get all cameras linked to a specific user.
     */
    public List<Camera> getCamerasByUserId(Long userId) {
    Optional<UserEntity> userOpt = userRepo.findById(userId);
    return userOpt.map(user -> new ArrayList<>(user.getCameras()))
                  .orElse(new ArrayList<>());
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
     * Link a camera to a user.
     * If the camera doesn't exist, create and assign it.
     * Allows multiple users to share the same camera.
     */
    public boolean linkCameraToUser(Map<String, Object> req) {
        try {
            Long userId = Long.parseLong(req.get("userId").toString());
            String deviceName = req.get("deviceName").toString();
            String cameraId = req.get("cameraId").toString();

            Optional<UserEntity> userOpt = userRepo.findById(userId);
            if (userOpt.isEmpty()) return false;

            UserEntity user = userOpt.get();

            // Fetch existing camera or create new
            Camera camera = cameraRepo.findByCameraId(cameraId).orElseGet(() -> {
                Camera newCam = new Camera();
                newCam.setCameraId(cameraId);
                newCam.setDeviceName(deviceName);
                newCam.setActive(true);
                newCam.setOnline(true);
                newCam.setUsers(new HashSet<>());
                return newCam;
            });

            // Update device name if camera already exists
            camera.setDeviceName(deviceName);
            camera.setActive(true);
            camera.setOnline(true);

            // Link user and camera bidirectionally
            camera.getUsers().add(user);
            user.getCameras().add(camera);

            cameraRepo.save(camera);
            userRepo.save(user);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Disconnect a user from a camera.
     * If no users are left, mark the camera as inactive and offline.
     */
    public boolean disconnectCamera(Map<String, Object> req) {
        try {
            String cameraId = req.get("cameraId").toString();
            Long userId = Long.parseLong(req.get("userId").toString());

            Optional<Camera> cameraOpt = cameraRepo.findByCameraId(cameraId);
            Optional<UserEntity> userOpt = userRepo.findById(userId);

            if (cameraOpt.isEmpty() || userOpt.isEmpty()) return false;

            Camera camera = cameraOpt.get();
            UserEntity user = userOpt.get();

            // Remove associations
            camera.getUsers().remove(user);
            user.getCameras().remove(camera);

            // If no users remain, mark camera inactive
            if (camera.getUsers().isEmpty()) {
                camera.setActive(false);
                camera.setOnline(false);
            }

            cameraRepo.save(camera);
            userRepo.save(user);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
