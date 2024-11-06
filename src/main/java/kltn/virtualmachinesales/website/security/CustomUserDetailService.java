package kltn.virtualmachinesales.website.security;


import kltn.virtualmachinesales.website.dto.UserPrincipal;
import kltn.virtualmachinesales.website.entity.user.User;
import kltn.virtualmachinesales.website.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

        return userDetails;
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        System.out.println("Loading user details for username: " + username);
//
//        User user = userRepository.findByUsername(username);
//        if (user == null) {
//            System.out.println("User not found in database");
//            throw new UsernameNotFoundException("User not found with username: " + username);
//        }
//
//        System.out.println("Found user: " + user.getUsername());
//        System.out.println("Stored password hash length: " + user.getPassword().length());
//
//        return new org.springframework.security.core.userdetails.User(
//                user.getUsername(),
//                user.getPassword(),
//                Collections.emptyList() // hoặc getAuthorities(user)
//        );
//    }

}
