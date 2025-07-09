package Sulekhaai.WHBRD.repository;

import Sulekhaai.WHBRD.model.Camera;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CameraRepository extends JpaRepository<Camera, Long> {
    List<Camera> findByUser_Id(Long userId);

    // ✅ Add this method to support disconnecting via string-based cameraId
    Optional<Camera> findByCameraId(String cameraId);
}
