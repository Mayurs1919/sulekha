package Sulekhaai.WHBRD.repository;

import Sulekhaai.WHBRD.model.RecordingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
 
public interface RecordingSessionRepository extends JpaRepository<RecordingSession, Long> {
    List<RecordingSession> findByUserId(Long userId);
} 