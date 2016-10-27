package hello;

import org.springframework.stereotype.Service;

@Service
public class ProductService {
	public String hello(String name) {
		return "Witaj s" + name;
	}
}
