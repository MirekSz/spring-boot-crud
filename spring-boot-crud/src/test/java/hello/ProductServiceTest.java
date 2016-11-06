package hello;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import hello.model.Product;
import hello.repo.ProductRepo;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class ProductServiceTest extends BaseTest {
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

	@Test
	public void shouldFindProductsWIthCustomQuery() {
		// given
		productRepo.save(new Product("Rower", "", 10, BigDecimal.TEN));
		productRepo.save(new Product("romet", "", 10, BigDecimal.TEN));

		// when
		List<Product> byName = productRepo.getByName("ro");
		Stream<Product> findByName = productRepo.findAllByName("romet");

		// then
		assertThat(byName).hasSize(2);
	}

	@Test
	public void shouldLoadFirstPage() {
		// given
		productRepo.save(new Product("Rower", "", 10, BigDecimal.TEN));
		productRepo.save(new Product("romet", "", 10, BigDecimal.TEN));

		// when
		Page<Product> all = productRepo.findAll(new PageRequest(0, 10));
		List<Product> content = all.getContent();

		// then
		assertThat(content).hasSize(2);
	}

}
