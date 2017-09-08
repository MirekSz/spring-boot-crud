
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import hello.Application;
import hello.RightsService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { Application.class })
@WebAppConfiguration
// @WebMvcTest(HomeController.class)
public class RightsServiceTest {
	@Autowired
	RightsService service;
	@Autowired
	private WebApplicationContext wac;

	private MockMvc mvc;
	@Autowired
	private Filter springSecurityFilterChain;

	@Before
	public void setup() {
		this.mvc = MockMvcBuilders.webAppContextSetup(wac).addFilters(springSecurityFilterChain).build();

	}

	@Test(expected = AccessDeniedException.class)
	public void shoulThrowAccessDeniedEx() throws Exception {
		setRole("USER");

		service.admin();
	}

	@Test
	public void shouldAllowAdmin() throws Exception {
		setRole("ADMIN");

		service.admin();
	}

	@WithMockUser(roles = { "ADMIN" })
	@Test
	public void shouldAllowAdminBYAnnotation() throws Exception {
		service.admin();
	}

	@Test
	public void mvc() throws Exception {
		String login = "a";
		mvc.perform(get("/admin").with(csrf()).with(user(login).roles("ADMIN")).accept(MediaType.TEXT_PLAIN))
				.andExpect(content().string(containsString(login)));
	}

	private void setRole(String role) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
		Authentication authToken = new UsernamePasswordAuthenticationToken("a", "b", authorities);
		SecurityContextHolder.getContext().setAuthentication(authToken);
	}
}
