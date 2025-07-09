package Sulekhaai.WHBRD.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String name;
    private String password;
    private String role;

    // ✅ NEW: Field for storing TOTP secret for Google Authenticator
    @Column(name = "otp_secret")
    private String otpSecret;

    @ManyToMany
    @JoinTable(
        name = "user_camera",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "camera_id")
    )
    private Set<Camera> cameras = new HashSet<>();

    // --- Getters & Setters ---

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public String getOtpSecret() { return otpSecret; }

    public void setOtpSecret(String otpSecret) { this.otpSecret = otpSecret; }

    public Set<Camera> getCameras() { return cameras; }
    public void setCameras(Set<Camera> cameras) {
    this.cameras = cameras;
}

}
