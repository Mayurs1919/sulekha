package Sulekhaai.WHBRD.repository;

import Sulekhaai.WHBRD.model.SessionImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SessionImageRepository extends JpaRepository<SessionImage, Long> {
    List<SessionImage> findBySessionId(Long sessionId);
} 