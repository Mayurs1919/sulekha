package Sulekhaai.WHBRD.services;

import Sulekhaai.WHBRD.repository.CameraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private CameraRepository cameraRepo;

    public Map<String, Object> getSummary() {
        long active = cameraRepo.findAll().stream().filter(c -> c.isActive()).count();
        long offline = cameraRepo.count() - active;
        return Map.of(
            "activeCameras", active,
            "offlineCameras", offline
        );
    }

    public Map<String, Object> getAnalytics() {
        return Map.of(
            "cpuUsage", "24%",
            "memoryUsage", "678MB",
            "totalRuntime", "5h 33m",
            "uptime", "99.97%"
        );
    }
}