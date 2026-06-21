package dio.proposalmanagement.infra.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
public class RestUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final ObjectMapper objectMapper;

  public RestUsernamePasswordAuthenticationFilter(AuthenticationConfiguration authenticationConfiguration,
                                                  ObjectMapper objectMapper) {
    super(authenticationConfiguration.getAuthenticationManager());
    this.objectMapper = objectMapper;
    setFilterProcessesUrl("/api/auth/login");
    setAuthenticationSuccessHandler((request, response, authentication) -> {
      // A sessão é criada automaticamente pelo Spring Security
      // Aqui apenas retornamos 200 OK
      response.setStatus(HttpServletResponse.SC_OK);
      response.getWriter().flush();
    });
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
    // You can customize the authentication process here, for example, by parsing JSON credentials from the request body

      try {
        LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

        var authToken = new UsernamePasswordAuthenticationToken(
          loginRequest.username(),
          loginRequest.password()
        );

        return getAuthenticationManager().authenticate(authToken);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
  }

  public record LoginRequest(String username, String password) {}

}
