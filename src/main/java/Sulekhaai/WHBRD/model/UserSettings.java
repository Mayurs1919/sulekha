package Sulekhaai.WHBRD.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_settings")
public class UserSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "headline")
    private String headline;

    @Column(name = "biography")
    private String biography;

    @Column(name = "language")
    private String language;

    @Lob
    @Column(name = "profile_photo", length = 16777215) // MEDIUMTEXT
    private String profilePhoto;

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getHeadline() { return headline; }
    public void setHeadline(String headline) { this.headline = headline; }
    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public String getProfilePhoto() { return profilePhoto; }
    public void setProfilePhoto(String profilePhoto) { this.profilePhoto = profilePhoto; }
}
