package kltn.virtualmachinesales.website.service;

import kltn.virtualmachinesales.website.dto.request.UserCreationDTO;
import kltn.virtualmachinesales.website.dto.request.UserEditDTO;
import kltn.virtualmachinesales.website.dto.response.UserViewDTO;
import kltn.virtualmachinesales.website.entity.user.User;
import kltn.virtualmachinesales.website.exceptions.EmailExistException;
import kltn.virtualmachinesales.website.exceptions.UsernameExistException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {

    void authenticate(String username, String password) throws Exception;

    UserViewDTO getUserDTOByEmail(String email);

//    UserViewDTO getUserViewDTOById(int userId);

//    UserViewDTO editUser(UserEditDTO userEditDTO);

    UserViewDTO register(UserCreationDTO userCreationDTO) throws EmailExistException, UsernameExistException;

//    UserViewDTO verifyEmail(int userId, String code);

//    int getUserIdByUserDetails(UserDetails userDetails);

//    List<UserViewDTO> listAllUserExceptMe(int userId);

    Boolean verifyUser(String gmail, String verifyCode);
    String verify(User user);
}
