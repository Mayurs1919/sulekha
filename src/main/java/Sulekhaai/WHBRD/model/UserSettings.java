package Sulekhaai.WHBRD.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_settings")
@Data
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String firstName;
    private String lastName;
    private String headline;
    private String biography;
    private String language;
    @Lob
    @Column(length = 100000)
    private String profilePhoto;
}
