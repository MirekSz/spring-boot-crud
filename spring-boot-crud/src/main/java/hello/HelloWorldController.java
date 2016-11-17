package hello;

import hello.repo.Auction;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
	@RequestMapping("/hello")
	public String hello() {
		if (2 > 1) {
			throw new NullPointerException("Uff");
		}
		return "Witaj23";
	}

	@RequestMapping("/add")
	public String add(@RequestBody @Valid Auction auction) {
		if (2 > 1) {
			throw new NullPointerException("Uff");
		}
		return "Witaj23";
	}
}
