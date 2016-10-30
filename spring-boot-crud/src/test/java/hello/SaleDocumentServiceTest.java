package hello;

import static org.assertj.core.api.Assertions.assertThat;
import hello.model.Product;
import hello.repo.ProductRepo;
import hello.service.DocumentRequest;
import hello.service.Item;
import hello.service.SaleDocumentService;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
@Rollback
@Transactional
@ActiveProfiles("test")
public class SaleDocumentServiceTest {
	@Inject
	private SaleDocumentService service;
	@Inject
	ProductRepo productRepo;

	@Test
	public void shouldUpdateStockWhenSaleProduct() {
		// given
		int BEGNING_STOCK = 100;
		Product save = productRepo.save(new Product("Rower", "", BEGNING_STOCK, BigDecimal.TEN));
		DocumentRequest documentRequest = new DocumentRequest();
		documentRequest.items.add(new Item(save.getId(), 3));

		// when
		service.insert(documentRequest);

		// then
		assertThat(save.getQuantity()).isLessThan(BEGNING_STOCK);
		assertThat(save.getQuantity()).isLessThan(BEGNING_STOCK);
	}
}
