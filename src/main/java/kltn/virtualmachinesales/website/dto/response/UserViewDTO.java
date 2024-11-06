package kltn.virtualmachinesales.website.dto.response;

import kltn.virtualmachinesales.website.entity.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserViewDTO {

    private int id;

    private String firstName;

    private String lastName;

    private String profile_pic_url;

    private String username;

    private String bio;

//    private Gender gender;

    private String email;

    public User ConvertDTOtoEntity() {
        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setProfile_pic_url(profile_pic_url);
        return user;
    }

}
