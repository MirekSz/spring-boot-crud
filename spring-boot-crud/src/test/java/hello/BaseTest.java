package hello;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@Rollback
@Transactional
@ActiveProfiles("test")
@ContextConfiguration
public abstract class BaseTest {
	public static class CsrfRequestPostProcessor implements RequestPostProcessor {

		private boolean useInvalidToken = false;

		private boolean asHeader = false;

		@Override
		public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
			CsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
			CsrfToken token = repository.generateToken(request);
			repository.saveToken(token, request, new MockHttpServletResponse());
			String tokenValue = useInvalidToken ? "invalid" + token.getToken() : token.getToken();
			if (asHeader) {
				request.setAttribute(token.getHeaderName(), token);
			} else {
				request.setAttribute(token.getParameterName(), token);
			}
			return request;
		}

		public RequestPostProcessor invalidToken() {
			this.useInvalidToken = true;
			return this;
		}

		public RequestPostProcessor asHeader() {
			this.asHeader = true;
			return this;
		}

	}

	public static CsrfRequestPostProcessor csrf() {
		return new CsrfRequestPostProcessor();
	}
}
