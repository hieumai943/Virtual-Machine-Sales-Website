package kltn.virtualmachinesales.website.controller.usercontroller;

import kltn.virtualmachinesales.website.dto.request.UserCreationDTO;
import kltn.virtualmachinesales.website.dto.request.UserLoginDTO;
import kltn.virtualmachinesales.website.dto.response.UserViewDTO;
import kltn.virtualmachinesales.website.entity.user.User;
import kltn.virtualmachinesales.website.exceptions.EmailExistException;
import kltn.virtualmachinesales.website.exceptions.UsernameExistException;
import kltn.virtualmachinesales.website.repository.UserRepository;
import kltn.virtualmachinesales.website.security.jwt.JwtUntil;
import kltn.virtualmachinesales.website.service.RedisService;
import kltn.virtualmachinesales.website.service.UserService;
import kltn.virtualmachinesales.website.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    JwtUntil jwtUntil;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/ping")
    public String Ping(){
        Map<String, String> hashmap = new HashMap<>();
        hashmap.put("ping", "pong");
        System.out.println();
        return "pong";
    }

//    @PostMapping("/login")
//    public ResponseEntity<Object> createAuthenticationToken(@RequestBody UserLoginDTO request) throws Exception {
//        User user = userRepository.findByUsername(request.getUsername());
//        userService.authenticate(user.getUsername(), request.getPassword());
//
//        final UserDetails userDetails = userDetailsService
//                .loadUserByUsername(user.getUsername());
//
//        UserViewDTO userViewDTO = userService.getUserDTOByEmail(user.getEmail());
//        String token = "123";
//
//        return ResponseHandler.generateAuthenticationResponse(userViewDTO, token);
//    }

    @PostMapping("/login")
    public ResponseEntity<String> createAuthenticationToken(@RequestBody UserLoginDTO request) throws Exception {
        User user = userRepository.findByUsername(request.getUsername());
        return ResponseEntity.ok(userService.verify(user.getUsername(), request.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUserAccount(@RequestBody UserCreationDTO userCreationDTO) throws UsernameExistException, EmailExistException {
        return ResponseEntity.ok(userService.register(userCreationDTO));
    }
    @PostMapping("/verify")
    public ResponseEntity<Boolean> verifyUser(@RequestParam String gmail, @RequestParam String verifyCode){

       return ResponseEntity.ok(userService.verifyUser(gmail, verifyCode));

    }


}
