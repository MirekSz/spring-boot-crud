package hello;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public UserDetailsService userDetails() throws Exception {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(
				User.withDefaultPasswordEncoder().username("user").password("user").roles("USER", "ACTUATOR").build());
		return manager;
	}

	// @Override
	// protected void configure(HttpSecurity http) throws Exception {
	// http.authorizeRequests().antMatchers("/resources/**", "/signup",
	// "/about").permitAll().antMatchers("/admin/**")
	// .hasRole("ADMIN").antMatchers("/actuator/**").access("hasRole('ADMIN')
	// and hasRole('DBA')").anyRequest()
	// .authenticated().and().formLogin().and().httpBasic().and().logout();
	// }
}
