package hello.ws;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class JWTAuthenticationFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		// Authentication authentication = new
		// TokenAuthenticationService().getAuthentication((HttpServletRequest)request);

		AuthenticatedUser authenticatedUser = new AuthenticatedUser("mirek");
		authenticatedUser.getAuthorities().add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken2 = new UsernamePasswordAuthenticationToken(
				authenticatedUser, "mirek");
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken2);
		filterChain.doFilter(request, response);
	}
}
