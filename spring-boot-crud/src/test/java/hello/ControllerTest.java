package hello;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.ViewController.Person;
import hello.model.Product;
import hello.repo.ProductRepo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@Rollback
@Transactional
@ActiveProfiles("test")
public class ControllerTest {
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
		controller.persons.add(person);

		// when
		String contentAsString = mvc.perform(get("/greeting")).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();

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
