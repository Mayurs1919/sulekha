<<<<<<< HEAD
package Sulekhaai.WHBRD.repository;
=======
    package Sulekhaai.WHBRD.repository;
>>>>>>> 45f4df5 (Updated Spring Boot backend: added, modified, and deleted files)

import Sulekhaai.WHBRD.model.SessionImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
 
public interface SessionImageRepository extends JpaRepository<SessionImage, Long> {
    List<SessionImage> findBySessionId(Long sessionId);
} 