package dio.proposalmanagement.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
//      .csrf().disable()
      .authorizeHttpRequests(auth -> auth
        .anyRequest().authenticated())
      .formLogin(Customizer.withDefaults());

    return http.build();
  }

  @Bean
  UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    UserDetails influencer = User.withUsername("influencer")
      .password(passwordEncoder.encode("password"))
      .roles("INFLUENCER")
      .build();

    UserDetails brand = User.withUsername("brand")
      .password(passwordEncoder.encode("password"))
      .roles("BRAND")
      .build();

    return new InMemoryUserDetailsManager(influencer, brand);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
