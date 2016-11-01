package hello;

import static org.assertj.core.api.Assertions.assertThat;

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
import hello.service.DocumentRequest;
import hello.service.Item;
import hello.service.SaleDocumentService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@Rollback
@Transactional
@ActiveProfiles("test")
public class SaleDocumentServiceTest {
	@Inject
	private SaleDocumentService service;
	@Inject
	ProductRepo productRepo;
	@MockBean
	SimpMessagingTemplate simpMessagingTemplate;

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
