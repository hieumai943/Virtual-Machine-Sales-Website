package kltn.virtualmachinesales.website.controller.usercontroller;

import kltn.virtualmachinesales.website.dto.request.UserCreationDTO;
import kltn.virtualmachinesales.website.dto.request.UserLoginDTO;
import kltn.virtualmachinesales.website.dto.response.UserViewDTO;
import kltn.virtualmachinesales.website.exceptions.EmailExistException;
import kltn.virtualmachinesales.website.exceptions.UsernameExistException;
import kltn.virtualmachinesales.website.security.jwt.JwtUntil;
import kltn.virtualmachinesales.website.service.UserService;
import kltn.virtualmachinesales.website.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    JwtUntil jwtUntil;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    UserService userService;




    @PostMapping("/login")
    public ResponseEntity<Object> createAuthenticationToken(@RequestBody UserLoginDTO request) throws Exception {

        userService.authenticate(request.getEmail(), request.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(request.getEmail());

        UserViewDTO userViewDTO = userService.getUserDTOByEmail(request.getEmail());
        String token = jwtUntil.generateToken(userDetails);

        return ResponseHandler.generateAuthenticationResponse(userViewDTO, token);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUserAccount(@RequestBody UserCreationDTO userCreationDTO) throws UsernameExistException, EmailExistException {
        return ResponseEntity.ok(userService.register(userCreationDTO));
    }

}
