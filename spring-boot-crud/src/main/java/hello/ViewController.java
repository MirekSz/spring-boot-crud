package hello;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.core.HttpHeaders;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import hello.model.Product;
import hello.repo.ProductRepo;
import hello.service.ProductChangeEvent;
import hello.service.ProductService;

@Controller
public class ViewController {
	@Inject
	ProductService productService;
	@Inject
	ProductRepo repo;

	@Value("classpath:/public/phones/*.jpg")
	Resource[] resources;
	List<Person> persons = new ArrayList();

	@Inject
	private SimpMessagingTemplate webSocket;

	@Inject
	ApplicationEventPublisher eventBus;

	@MessageMapping("/topic/hello")
	public void greeting(Map<String, Object> message) throws Exception {
		webSocket.convertAndSend("/queue/mirek/priv", "Witam " + message.get("name"));
	}

	@PostConstruct
	public void init() {
		for (int i = 0; i < 5; i++) {
			Person person = new Person();
			person.setName("Name" + i);
			person.id = (long) i;
			person.phone = resources[persons.size()].getFilename();
			persons.add(person);
		}
	}

	@RequestMapping(value = "/greeting", method = RequestMethod.GET)
	public String greeting(Model model,
			@RequestHeader(value = "X-Requested-With", required = false) String ajaxHeader) {
		model.addAttribute("person", new Person());
		if (ajaxHeader != null) {
			return "greeting :: lista";

		}
		return "greeting";
	}

	@RequestMapping("/download")
	@ResponseBody
	public ResponseEntity<Resource> download() throws Exception {
		Resource file = new UrlResource(new File("C:\\Users\\Mirek\\Desktop\\pro.jpg").toURI());
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
				// .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;
				// filename=\"" +
				// file.getFilename() + "\"")
				.body(file);
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public void save(@RequestParam("file") List<MultipartFile> file) {
		System.out.println(file.size());
	}

	@RequestMapping(value = "/greeting", method = RequestMethod.POST)
	public String save(@RequestParam("file") List<MultipartFile> file, @ModelAttribute("person") @Valid Person person,
			BindingResult bs) {
		if (bs.hasErrors()) {
			return "greeting";
		}
		Future<Long> rep1 = productService.veryReportGeneration(false);
		Future<Long> rep2 = productService.veryReportGeneration(true);
		Future<Long> rep3 = productService.veryReportGeneration(false);
		// while (!rep1.isDone() || !rep2.isDone() || !rep3.isDone()) {
		// try {
		// Thread.sleep(100);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		System.out.println("ALL DONE");
		person.phone = resources[persons.size()].getFilename();
		persons.add(0, person);
		eventBus.publishEvent(new ProductChangeEvent());
		return "redirect:/greeting";
	}

	public static class Person {
		private Long id;
		@NotEmpty
		private String name;
		private String phone;
		private List<Tag> tags = new ArrayList<>();

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public List<Tag> getTags() {
			return tags;
		}

		public void setTags(List<Tag> tags) {
			this.tags = tags;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}
	}

	public static class Tag {
		private Long id;
		private String name;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	@RequestMapping(value = "/asJSON", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> asJson(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
		Map<String, Object> res = new HashMap<>();
		res.put("items", persons.subList((page - 1) * 10, ((page - 1) * 10) + 10));
		res.put("total_count", 100);
		res.put("page", page);
		return res;
	}

	@RequestMapping(value = "/products", method = RequestMethod.GET)
	@ResponseBody
	public List<Product> products() {
		return repo.findAll();
	}

	@ModelAttribute("persons")
	public List<Person> provideList() {
		return persons;
	}

}
