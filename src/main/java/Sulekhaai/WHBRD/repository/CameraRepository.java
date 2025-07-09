package Sulekhaai.WHBRD.repository;

import Sulekhaai.WHBRD.model.Camera;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CameraRepository extends JpaRepository<Camera, Long> {

    // ✅ FIXED: Correct method name for ManyToMany user-camera lookup
    List<Camera> findByUsers_Id(Long userId);

    // ✅ Used for linking/disconnecting by string-based cameraId
    Optional<Camera> findByCameraId(String cameraId);
}
