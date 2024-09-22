package kltn.virtualmachinesales.website.service.implement;

import kltn.virtualmachinesales.website.dto.response.UserViewDTO;
import kltn.virtualmachinesales.website.dto.request.UserCreationDTO;
import kltn.virtualmachinesales.website.entity.user.User;
import kltn.virtualmachinesales.website.exceptions.EmailExistException;
import kltn.virtualmachinesales.website.exceptions.UsernameExistException;
import kltn.virtualmachinesales.website.repository.UserRepository;
import kltn.virtualmachinesales.website.service.RedisService;
import kltn.virtualmachinesales.website.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private RedisService redisService;

    public UserServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @Override
    public UserViewDTO getUserDTOByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user.ConvertEntitytoDTO();
    }

//    @Override
//    public UserViewDTO getUserViewDTOById(int userId) {
//        User user = userRepository.findById(userId);
//        return mapper.map(user, UserViewDTO.class);
//    }
//
//    @Override
//    @Transactional
//    public UserViewDTO editUser(UserEditDTO userEditDTO) {
//        User user = userRepository.findById(userEditDTO.getId());
//        user.setFirstName(userEditDTO.getFirstName());
//        user.setLastName(userEditDTO.getLastName());
//        user.setBio(userEditDTO.getBio());
//        user.setGender(userEditDTO.getGender());
//
//        if (userEditDTO.getFile() != null) {
//            CloudinaryResponseDTO cloudinaryResponse = fileUploadService.uploadFile(userEditDTO.getFile(), userEditDTO.getFile().getOriginalFilename());
//            user.setProfile_pic_url(cloudinaryResponse.getUrl());
//        }
//
//        userRepository.save(user);
//        return mapper.map(user, UserViewDTO.class);
//    }


    @Override
    public UserViewDTO register(UserCreationDTO userCreationDTO) throws EmailExistException, UsernameExistException {
        User user = userRepository.findByEmail(userCreationDTO.getEmail());
        if (user != null) {
            throw new EmailExistException("User with email " + userCreationDTO.getEmail() + " already exists");
        }

        if (userRepository.existsByUsername(userCreationDTO.getUsername())) {
            throw new UsernameExistException("User with username exists");
        }

        user = new User(userCreationDTO.getFirstName(), userCreationDTO.getLastName(), userCreationDTO.getGender(), userCreationDTO.getEmail(), userCreationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userCreationDTO.getPassword()));

        userRepository.save(user);
//        kafkaTemplate.send("send-email", String.valueOf(user.getId()));
        return user.ConvertEntitytoDTO();
    }

//    @Override
//    public UserViewDTO verifyEmail(int userId, String Code) {
//        User user = userRepository.findById(userId);
//        Object verifyCode = redisService.find(user.getEmail());
//        if (verifyCode.equals(Code)) {
//            user.setEmailStatus(EmailStatus.Verified);
//        }
//        userRepository.save(user);
//        return mapper.map(user, UserViewDTO.class);
//    }
//
//    @Override
//    public int getUserIdByUserDetails(UserDetails userDetails) {
//        String email = userDetails.getUsername();
//        User user = userRepository.findByEmail(email);
//
//        return user.getId();
//    }
//
//    @Override
//    public List<UserViewDTO> listAllUserExceptMe(int userId) {
//        List<User> userList = userRepository.listAllUserExceptMe(userId);
//
//        List<UserViewDTO> userViewDTOList = new ArrayList<>();
//        userList.forEach(user -> {
//            userViewDTOList.add(mapper.map(user, UserViewDTO.class));
//        });
//
//        return userViewDTOList;
//
//    }
    @Override
    public Boolean verifyUser(String gmail, String verifyCode){
        if(redisService.find(gmail) != null){
            if(redisService.find(gmail).toString().equals(verifyCode)){
                return true;
            }
        }
        return false;
    }

}
