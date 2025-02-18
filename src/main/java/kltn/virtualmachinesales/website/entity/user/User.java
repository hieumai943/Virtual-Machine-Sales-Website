package kltn.virtualmachinesales.website.entity.user;


import jakarta.persistence.*;
import kltn.virtualmachinesales.website.dto.response.UserViewDTO;
import kltn.virtualmachinesales.website.entity.enums.EmailStatus;
import kltn.virtualmachinesales.website.entity.enums.Gender;
import kltn.virtualmachinesales.website.entity.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String email;

    private String username;

    private String password;

    private String bio;

    private String profile_pic_url;

    @Enumerated(EnumType.STRING)
    private Role role;

//    @Enumerated(EnumType.STRING)
//    private EmailStatus emailStatus;

    public User(String firstName, String lastName, Gender gender, String email, String username, String password, String bio, String profile_pic_url) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.profile_pic_url = profile_pic_url;
    }

    public User(String firstName, String lastName, Gender gender, String email, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(String firstName, String lastName, Gender gender, String email, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.username = username;
    }



    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public UserViewDTO ConvertEntitytoDTO() {
        UserViewDTO userDTO = new UserViewDTO();
        userDTO.setId(id);
        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);
        userDTO.setProfile_pic_url(profile_pic_url);
        userDTO.setUsername(username);
        return userDTO;
    }
}