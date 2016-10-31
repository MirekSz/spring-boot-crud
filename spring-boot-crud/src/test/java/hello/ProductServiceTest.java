package hello;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import hello.model.Product;
import hello.repo.ProductRepo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@Rollback
@Transactional
@ActiveProfiles("test")
public class ProductServiceTest {
	@Inject
	private ProductService service;
	@Inject
	ProductRepo productRepo;
	@MockBean
	SimpMessagingTemplate simpMessagingTemplate;

	@Test
	public void shouldAddProductAndInformByWebSocket() {
		// given
		Product product = new Product("Rower", "", 10, BigDecimal.TEN);
		long initialCount = productRepo.count();

		// when
		service.save(product);

		// then
		assertThat(productRepo.count()).isGreaterThan(initialCount);
		verify(simpMessagingTemplate).convertAndSend(anyString(), eq(true));
	}
}
