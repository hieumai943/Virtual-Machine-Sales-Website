package kltn.virtualmachinesales.website.security;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configurable
public class UserSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(config -> config.anyRequest().authenticated())       // any request must be authenticated
                .formLogin(form -> form.loginPage("/showMyLoginPage")
                .loginProcessingUrl("/authenticateUser")   // để mà có thể login dc thì phải call đến URL này
                        .permitAll());          // tất cả mọi người đều có thể vào dc trang login
      return   http.build();

    }
}
