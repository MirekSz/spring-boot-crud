package hello;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import hello.model.Product;
import hello.repo.ProductRepo;
import hello.repo.SaleDocumentRepo;
import hello.service.DocumentRequest;
import hello.service.Item;
import hello.service.SaleDocumentService;

public class SaleDocumentServiceTest extends BaseTest {
	@Inject
	private SaleDocumentService service;
	@Inject
	ProductRepo productRepo;
	@Inject
	SaleDocumentRepo saleDocumentRepo;
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

		// then@EntityGraph
		assertThat(save.getQuantity()).isLessThan(BEGNING_STOCK);
		assertThat(save.getQuantity()).isLessThan(BEGNING_STOCK);
	}

	@Test
	public void saderFetching() {
		// given
		saleDocumentRepo.findAll();
		saleDocumentRepo.findTop10ByOrderByNumberDesc();

		// when

		// then@EntityGraph
	}
}
