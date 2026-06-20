package dio.proposalmanagement.infra.http;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class Controller {

  @GetMapping()
  public String hello(@AuthenticationPrincipal UserDetails user) {
    return "Hello, World " + user.getUsername();
  }

  @GetMapping("/influencer")
  @PreAuthorize("hasRole('INFLUENCER')")
  public String influencerEndpoint(@AuthenticationPrincipal UserDetails user) {
    return "Hello, Influencer " + user.getUsername();
  }

  @GetMapping("/brand")
  @PreAuthorize("hasRole('BRAND')")
  public String brandEndpoint(@AuthenticationPrincipal UserDetails user) {
    return "Hello, Brand " + user.getUsername();
  }

}
