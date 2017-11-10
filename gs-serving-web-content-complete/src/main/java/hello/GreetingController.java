package hello;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
public class GreetingController {
	@Autowired
	ProuctRepo repo;

	@Autowired
	CronJon cronJon;

	@GetMapping("/greeting")
	public String greeting(@RequestParam(value = "query", required = false) String query, Model model) {
		model.addAttribute("name", query);
		model.addAttribute("products", getProducts(query));
		model.addAttribute("product", new Product("a", "a", 12, BigDecimal.TEN));
		return "greeting";
	}

	@PostMapping("/greeting")
	public String greeting(@ModelAttribute("product") Product product) {
		repo.save(product);
		return "redirect:/greeting";
	}

	@GetMapping("/productsStream")
	SseEmitter productsStream() {
		SseEmitter emitter = new SseEmitter();
		cronJon.addEmitter(emitter);
		return emitter;
	}

	@GetMapping("/api/products")
	@ResponseBody
	List<Product> getProducts(String query) {
		if (query == null) {
			return repo.findAll();
		}
		return repo.findByNameStartsWith(query);
	}

}
