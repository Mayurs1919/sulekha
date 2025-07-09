package Sulekhaai.WHBRD.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Camera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String cameraId;

    private String deviceName;
    private boolean active;
    private boolean online;

    @ManyToMany(mappedBy = "cameras")
    private Set<UserEntity> users = new HashSet<>();

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getCameraId() { return cameraId; }

    public void setCameraId(String cameraId) { this.cameraId = cameraId; }

    public String getDeviceName() { return deviceName; }

    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }

    public boolean isActive() { return active; }

    public void setActive(boolean active) { this.active = active; }

    public boolean isOnline() { return online; }

    public void setOnline(boolean online) { this.online = online; }

    public Set<UserEntity> getUsers() { return users; }

    public void setUsers(Set<UserEntity> users) { this.users = users; }
}
