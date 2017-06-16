package hello;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class RightsService {
	@PreAuthorize("hasAnyRole('ROLE_PARTNER')")
	public String partner() {
		return "ws";
	}

	@PreAuthorize("hasAnyRole('ROLE_USER')")
	public String user() {
		return "ws";
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public String admin() {
		return "ws";
	}
}
