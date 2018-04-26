
package sample;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class OAuth2LoginSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().logout().and().oauth2Login().userInfoEndpoint()
				.customUserType(GitHubOAuth2User.class, "github");// .userService(this.oauth2UserService());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	// private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
	// return new OAuth2UserService() {
	//
	// @Override
	// public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
	// // TODO Auto-generated method stub
	// return null;
	// }
	// };
	// }

}
