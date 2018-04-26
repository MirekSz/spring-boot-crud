
package sample;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import sample.api.facebook.Facebook;

@Controller
public class HomeController {

	private final Facebook facebook;
	@Autowired
	OAuth2AuthorizedClientService clientService;

	@Autowired
	public HomeController(final Facebook facebook) {
		this.facebook = facebook;
	}

	@GetMapping("/")
	public String home(final Model model, final Principal principal) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String accessToken = null;
		if (authentication.getClass().isAssignableFrom(OAuth2AuthenticationToken.class)) {
			OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
			String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
			OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(clientRegistrationId, oauthToken.getName());
			accessToken = client.getAccessToken().getTokenValue();
		}

		// model.addAttribute("profile", facebook.getProfile());
		// model.addAttribute("feed", facebook.getFeed());ssss
		model.addAttribute("principal", principal);
		model.addAttribute("accessToken", accessToken);
		return "home";
	}

}
