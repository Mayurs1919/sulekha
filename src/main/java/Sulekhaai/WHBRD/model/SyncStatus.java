// SyncStatus.java
package Sulekhaai.WHBRD.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class SyncStatus {
    @Id
    private String email;           // one‑row per user
    private String status;
    private java.time.LocalDateTime lastSynced;
}
