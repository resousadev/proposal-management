package dio.proposalmanagement.infra.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    setFilterProcessesUrl("/api/auth/login"); // Set the login endpoint # after habilitar o formLogin() no SecurityConfig, o endpoint de login passa a ser /login, então precisamos configurar o filtro para processar as requisições nesse endpoint
    setAuthenticationSuccessHandler((request, response, authentication) ->
        response.setStatus(HttpServletResponse.SC_OK)); // Return 200 OK on successful authentication
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
