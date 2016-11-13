package hello;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import hello.ViewController.Person;
import hello.model.Product;
import hello.repo.ProductRepo;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ControllerTest extends BaseTest {
	private MockMvc mvc;
	@Inject
	ViewController controller;
	@Inject
	ProductRepo productRepo;
	@MockBean
	SimpMessagingTemplate simpMessagingTemplate;
	@Inject
	private WebApplicationContext wac;
	private ObjectMapper objectMapper;

	@Before
	public void setup() {
		ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
		thymeleafViewResolver.setTemplateEngine(new SpringTemplateEngine());
		this.mvc = MockMvcBuilders.webAppContextSetup(wac).build();

		objectMapper = new ObjectMapper();
	}

	@Test
	public void shouldGetAllPersons() throws Exception {
		// given
		Person person = new Person();
		person.setName("Stefan");
		controller.provideList().add(person);

		// when
		String contentAsString = mvc.perform(get("/greeting").with(csrf())).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();

		// then
		assertThat(contentAsString).contains("Stefan");
	}

	@Test
	public void shouldGetAllProductsAsJSON() throws Exception {
		// given
		Product product = new Product("Rower", "", 10, BigDecimal.TEN);
		productRepo.save(product);

		// when
		String contentAsString = mvc.perform(get("/products")).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();

		// then
		Product[] readValue = objectMapper.readValue(contentAsString, Product[].class);
		assertThat(readValue).hasSize((int) productRepo.count());
	}
}
