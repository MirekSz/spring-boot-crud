
package org.baeldung.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JWTLoginFilter extends OncePerRequestFilter {

	private final AuthenticationManager authManager;

	public JWTLoginFilter(final AuthenticationManager authManager) {
		this.authManager = authManager;
	}

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
			throws ServletException, IOException {
		if (request.getQueryString() != null && request.getQueryString().contains("jwt")) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null && request.getHeader(TokenAuthenticationService.HEADER_STRING) == null) {
				TokenAuthenticationService.addAuthentication(response, authentication.getName());
			}
		}
		filterChain.doFilter(request, response);
	}

}
