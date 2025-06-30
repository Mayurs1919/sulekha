package Sulekhaai.WHBRD.repository;

import Sulekhaai.WHBRD.model.CameraLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CameraLogRepository extends JpaRepository<CameraLog, Long> {
    List<CameraLog> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}