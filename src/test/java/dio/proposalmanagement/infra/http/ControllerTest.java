package dio.proposalmanagement.infra.http;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ControllerTest {

  @InjectMocks
  private Controller controller;

  @Mock
  private UserDetails user;

  @Mock
  private HttpSession session;

  @Test
  void shouldReturnGreetingForAuthenticatedUser() {
    when(user.getUsername()).thenReturn("ana");

    assertEquals("Hello, World ana", controller.hello(user));
  }

  @Test
  void shouldReturnGreetingForInfluencer() {
    when(user.getUsername()).thenReturn("ana");

    assertEquals("Hello, Influencer ana", controller.influencerEndpoint(user));
  }

  @Test
  void shouldReturnGreetingForBrand() {
    when(user.getUsername()).thenReturn("acme");

    assertEquals("Hello, Brand acme", controller.brandEndpoint(user));
  }

  @Test
  void shouldReturnSessionInformation() {
    when(user.getUsername()).thenReturn("ana");
    when(session.getId()).thenReturn("session-123");
    when(session.getCreationTime()).thenReturn(1_725_000_000_000L);

    assertEquals(
        "User: ana, Session ID: session-123, Session Created At: 1725000000000",
        controller.sessionInfo(user, session));
  }
}
