
package org.baeldung.security;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenAuthenticationService {

	static final long EXPIRATIONTIME = 864_000_000; // 10 days
	static final String SECRET = "ThisIsASecret";
	static final String TOKEN_PREFIX = "Bearer";
	static final String HEADER_STRING = "Authorization";

	public static void addAuthentication(final HttpServletResponse res, final String username) {
		String JWT;
		try {
			JWT = Jwts.builder().setSubject(username).setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
					.signWith(SignatureAlgorithm.HS512, SECRET.getBytes("UTF-8")).claim("ROLE", Arrays.asList("FACE", "ADMIN")).compact(); // .getBytes("UTF-8")
																																			// https://jwt.io/#debugger
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
	}

	public static Authentication getAuthentication(final HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		if (token != null) {
			// parse the token.
			String user;
			List<String> roles;
			try {
				Claims body =
						Jwts.parser().setSigningKey(SECRET.getBytes("UTF-8")).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody();
				roles = (List<String>) body.get("ROLE");
				user = body.getSubject();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return user != null
					? new UsernamePasswordAuthenticationToken(user, null, roles.stream().map(SimpleGrantedAuthority::new).collect(toList()))
					: null;
		}
		return null;
	}
}
