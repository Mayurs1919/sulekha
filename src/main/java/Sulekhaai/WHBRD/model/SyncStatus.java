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

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public java.time.LocalDateTime getLastSynced() { return lastSynced; }
    public void setLastSynced(java.time.LocalDateTime lastSynced) { this.lastSynced = lastSynced; }
}
