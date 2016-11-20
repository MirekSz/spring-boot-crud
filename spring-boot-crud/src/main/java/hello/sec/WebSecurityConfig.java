package hello.sec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.repository.query.spi.EvaluationContextExtension;
import org.springframework.data.repository.query.spi.EvaluationContextExtensionSupport;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

	@EventListener
	public void handle(AuthorizationFailureEvent envent) {
		System.out.println(envent);
	}

	@Configuration
	@Order(2)
	public static class FormConfig extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			// http.addFilterBefore(filter,
			// UsernamePasswordAuthenticationFilter.class);
			http.antMatcher("/**").authorizeRequests()
					.antMatchers("/assets/**", "/login**", "/logout**", "/services/**").permitAll().anyRequest()
					.authenticated().and().formLogin().loginPage("/login").and().logout().logoutUrl("/logout").and()
					.rememberMe().and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
					.exceptionHandling().and().httpBasic()
					.authenticationEntryPoint(new AjaxAwareAuthenticationEntryPoint("/login"));
			http.headers().frameOptions().disable();

			// AccessDeniedHandlerImpl deniedhandler = new
			// AccessDeniedHandlerImpl();
			// deniedhandler.setErrorPage("/accessdenied");
			// http.authorizeRequests()
			// .antMatchers("/welcome", "/api/ping", "/api/cookie", "/signup",
			// "loginAjax", "/about", "/register", "/currentUser", "/",
			// "/welcome")
			// .permitAll().antMatchers("/api/admin/**").hasRole("ADMIN")
			// .antMatchers("/api/appContext").hasRole("ADMIN")
			// .antMatchers("/role/**").hasRole("ADMIN")
			// .antMatchers("/role/*").hasRole("ADMIN")
			// /* .antMatchers("/products/**").hasAnyRole("USER")
			// .antMatchers("/products/*").hasAnyRole("USER")*/
			// .antMatchers("/api/user/**").hasRole("USER")
			// .antMatchers("/currentUser").hasRole("USER")
			// .antMatchers("/api/business**").access("hasRole('ROLE_ADMIN') and
			// hasRole('ROLE_BUSINESS')").anyRequest().authenticated();
			// http.headers().disable();
			// http.csrf().disable()
			// .formLogin().loginPage("/login").successHandler(authSuccessHandlerImpl).failureHandler(authFailureHandlerImpl).failureUrl("/login?error=true").defaultSuccessUrl("/").permitAll()
			// .and()
			// .logout().logoutUrl("/logout").logoutSuccessUrl("/welcome").invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll()
			// .and().exceptionHandling().accessDeniedHandler(deniedhandler);
			// //@formatter:on
		}

		private CsrfTokenRepository csrfTokenRepository() {
			HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
			// repository.setHeaderName("sec-token");
			return repository;
		}

		// @Autowired
		// public void configureGlobal(UserDetailsService userDetailsService,
		// AuthenticationManagerBuilder auth,
		// PasswordEncoder passwordEncoder) throws Exception {
		// auth.userDetailsService(userDetailsService);
		// }

		@Autowired
		public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
			// @formatter:off
			auth.userDetailsService(new UserDetailsService() {

				@Override
				public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException {
					return new SystemUser();
				}
			});// .passwordEncoder(new BCryptPasswordEncoder());
				// auth.inMemoryAuthentication().withUser("mirek").password("mirek").roles("USER");

		}
	}

	@Configuration
	@Order(1)
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication().withUser("api").password("pass").roles("API");
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			// .hasRole("API")
			http.antMatcher("/api/**").authorizeRequests().antMatchers("/**").authenticated().and().httpBasic().and()
					.csrf().disable();
		}
	}

	@Bean
	public EvaluationContextExtension securityExtension() {
		return new SecurityEvaluationContextExtensionImpl();
	}

	public static class SecurityEvaluationContextExtensionImpl extends EvaluationContextExtensionSupport {
		@Override
		public String getExtensionId() {
			return "security";
		}

		@Override
		public Object getRootObject() {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			return new SecurityExpressionRoot(authentication) {
			};
		}
	}

}
