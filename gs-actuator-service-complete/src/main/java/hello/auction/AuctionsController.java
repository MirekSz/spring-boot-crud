package hello.auction;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hello.async.BackgroundTask;
import reactor.core.publisher.Flux;

@Controller
@RequestMapping("/auctions")
public class AuctionsController {
	@Autowired
	AuctionRepo repo;
	@Autowired
	BackgroundTask backgroundTask;

	@RequestMapping(path = "/stream", method = RequestMethod.GET)
	public SseEmitter stream() throws IOException {

		SseEmitter emitter = new SseEmitter();
		backgroundTask.add(emitter);
		return emitter;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@RequestMapping("/abc")
	@GetMapping
	Flux<Auction> allMessages() {
		return Flux.just(new Auction(1L, "Dysk SSD", "Dysk SSD", BigDecimal.TEN));
	}

	@PostConstruct
	public void addData() {
		repo.save(new Auction(1L, "Dysk SSD", "Dysk SSD", BigDecimal.TEN));
		repo.save(new Auction(2L, "DDR 16GB", "DDR 16GB", BigDecimal.TEN));
	}

	@RequestMapping
	public String list(Model model) {
		model.addAttribute("auctions", repo.findAll());
		model.addAttribute("demo", new Date());
		return "auction/auction-list";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model) {
		if (ssEmitter != null) {
			try {
				ssEmitter.send("creating new auction");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Auction auction = new Auction();
		auction.setExpireDate(LocalDate.now());
		model.addAttribute("auction", auction);
		return "auction/auction-form";
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable("id") Long id, Model model) {
		repo.findById(id).ifPresent(e -> {
			model.addAttribute("auction", e);
		});
		return "auction/auction-form";
	}

	@RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
	public String edit(@ModelAttribute Auction auction, @RequestParam("file") MultipartFile uploadfile)
			throws Exception {
		saveFile(uploadfile);
		repo.save(auction);
		return "redirect:/auctions";
	}

	@Autowired
	Validator validator;
	private SseEmitter ssEmitter;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@ModelAttribute Auction auction, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, @RequestParam("file") MultipartFile uploadfile) throws Exception {
		saveFile(uploadfile);
		handle(auction, bindingResult);
		if (bindingResult.hasErrors()) {
			return "auction/auction-form";
		}

		repo.save(auction);
		redirectAttributes.addFlashAttribute("added", true);
		return "redirect:/auctions";
	}

	private void saveFile(MultipartFile uploadfile) throws IOException {
		if (uploadfile != null && !uploadfile.isEmpty()) {
			String originalFilename = uploadfile.getOriginalFilename();
			File file = new File("");
			String absolutePath = file.getAbsolutePath();
			String savePath = absolutePath + File.separator + "src" + File.separator + "main" + File.separator
					+ "resources" + File.separator + "public" + File.separator + "phones";
			FileUtils.copyInputStreamToFile(uploadfile.getInputStream(),
					new File(savePath + File.separator + originalFilename));
		}
	}

	public void handle(Auction auction, BindingResult bindingResult) {
		Set<ConstraintViolation<Auction>> validate = validator.validate(auction);
		// bindingResult.reject("zle", "Jest zle");
		if (validate.size() > 0) {
			for (ConstraintViolation<Auction> constraintViolation : validate) {
				bindingResult.rejectValue(constraintViolation.getPropertyPath().toString(), "NotNull");

			}
		}

	}
}
