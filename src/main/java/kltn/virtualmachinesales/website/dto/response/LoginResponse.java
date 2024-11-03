package kltn.virtualmachinesales.website.dto.response;

import kltn.virtualmachinesales.website.entity.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private Role role;
}
