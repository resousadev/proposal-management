package dio.proposalmanagement.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http,
                                  RestUsernamePasswordAuthenticationFilter restUsernamePasswordAuthenticationFilter)
      throws Exception {
    http
      .csrf(AbstractHttpConfigurer::disable) // Always consult what type of application client will be consuming your API before disabling CSRF

        .securityContext(context -> context.requireExplicitSave(false)) // Don't save the security context in the session after each request

        .authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/auth/**").permitAll() // Allow unauthenticated access to authentication endpoints
        .anyRequest().authenticated())

      .addFilterAt(restUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

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
