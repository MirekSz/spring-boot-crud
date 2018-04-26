
package com.example.keyklockdemo;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class KeyklockDemoApplication {

	public static void main(final String[] args) {
		SpringApplication.run(KeyklockDemoApplication.class, args);
	}

	@Controller
	class HomeController {

		@GetMapping("/")
		public String red() {
			return "redirect:/home";
		}

		@GetMapping("/home")
		public String home(final Model model, final Principal principal, final HttpSession session) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			model.addAttribute("principal", principal);
			model.addAttribute("sessionId", session.getId());
			return "home";
		}
	}
}
