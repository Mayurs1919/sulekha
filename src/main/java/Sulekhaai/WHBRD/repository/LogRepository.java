package Sulekhaai.WHBRD.repository;

import Sulekhaai.WHBRD.model.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<LogEntry, Long> {
}