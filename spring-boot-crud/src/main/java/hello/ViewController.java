package hello;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ViewController {
	List<String> persons = new ArrayList();

	@RequestMapping(value = "/greeting", method = RequestMethod.GET)
	public String greeting(
			Model model,
			@RequestParam(required = false) String name,
			@RequestHeader(value = "X-Requested-With", required = false) String ajaxHeader) {
		model.addAttribute("value",
				"<script>$(document.body).empty();</script>");
		if (name != null) {
			persons.add(name);
		}
		if (ajaxHeader != null) {
			return "greeting :: lista";

		}
		return "greeting";
	}

	@RequestMapping(value = "/asJson", method = RequestMethod.GET)
	@ResponseBody
	public List<String> asJson() {
		return persons;
	}

	@ModelAttribute("persons")
	public List<String> provideList() {
		return persons;
	}

}
