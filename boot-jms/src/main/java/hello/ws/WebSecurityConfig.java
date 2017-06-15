package hello.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.expressionHandler(webExpressionHandler());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		CookieCsrfTokenRepository withHttpOnlyFalse = CookieCsrfTokenRepository.withHttpOnlyFalse();
		withHttpOnlyFalse.setHeaderName("abc");
		http.authorizeRequests().antMatchers("/", "/*.js").permitAll().antMatchers("/").hasAnyRole("USER")
				.antMatchers("/ws").hasAnyRole("ADMIN").anyRequest().authenticated().and().formLogin().permitAll().and()
				.logout().permitAll().and().sessionManagement()
				.sessionAuthenticationStrategy(sessionAuthenticationStrategy()).and().csrf()
				.csrfTokenRepository(withHttpOnlyFalse);
	}

	@Bean
	protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
		return new RegisterSessionAuthenticationStrategy(sessionRegistry());
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("user").password("user").roles("USER").and().withUser("mirek")
				.password("mirek").roles("ADMIN");
	}

	// #####################################################################################
	@Bean
	public RoleHierarchyImpl roleHierarchy() {
		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
		roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER and  ROLE_USER > ROLE_PARTNER ");
		return roleHierarchy;
	}

	@Bean
	public DefaultWebSecurityExpressionHandler webExpressionHandler() {
		DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
		defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
		return defaultWebSecurityExpressionHandler;
	}
	//
	// @Bean
	// public AffirmativeBased accessDecisionManager() {
	// WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
	// webExpressionVoter.setExpressionHandler(webExpressionHandler());
	// AffirmativeBased decisionManager = new AffirmativeBased(
	// Arrays.asList(webExpressionVoter, new
	// RoleHierarchyVoter(roleHierarchy())));
	// decisionManager.setAllowIfAllAbstainDecisions(false);
	// return decisionManager;
	// }
}
