package hello;

import hello.mrepo.AuctionMRepo;
import hello.repo.Auction;
import hello.repo.AuctionRepo;
import hello.service.SaleDocumentService;
import hello.www.DummyProductService;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auctions")
public class AuctionsController {
	@Autowired
	AuctionRepo repo;
	@Autowired
	AuctionMRepo mrepo;

	@Autowired
	DummyProductService productService;
	@Autowired
	SaleDocumentService service;
	@Autowired
	UserContext userContext;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@PostConstruct
	public void addData() {
		repo.save(new Auction(1L, "Dysk SSD", "Dysk SSD", BigDecimal.TEN));
		repo.save(new Auction(2L, "DDR 16GB", "DDR 16GB", BigDecimal.TEN));
	}

	@RequestMapping
	public String list(Model model) {
		if (ssEmitter != null) {
			try {
				ssEmitter.send("halo");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<Auction> findAllWhereActivesIsTrue = repo.findTop10ByActiveIsTrue();
		System.out.println("service " + service.getClass());
		List<Auction> findAll = mrepo.findAll();
		findAll = mrepo.findAll();
		Auction findById = mrepo.findById(1L);
		userContext.put("jeden", findById);
		findById = mrepo.findById(1L);
		List<Auction> allForCurrentUser = repo.getAllForCurrentUser();
		System.out.println("allForCurrentUser " + allForCurrentUser.size());
		Auction findOne = repo.findOne(repo.findAll().get(0).getId());
		model.addAttribute("auctions", repo.findAll());
		model.addAttribute("demo", new Date());
		productService.list();
		return "auction/auction-list";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model) {
		Auction auction = new Auction();
		auction.setExpireDate(new Date());
		model.addAttribute("auction", auction);
		return "auction/auction-form";
	}

	@Autowired
	Validator validator;
	private SseEmitter ssEmitter;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@ModelAttribute Auction auction, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, @RequestParam("file") MultipartFile uploadfile) throws Exception {
		if (uploadfile != null && !uploadfile.isEmpty()) {
			String originalFilename = uploadfile.getOriginalFilename();
			File file = new File("");
			String absolutePath = file.getAbsolutePath();
			String savePath = absolutePath + File.separator + "src" + File.separator + "main" + File.separator
					+ "resources" + File.separator + "public" + File.separator + "phones";
			FileUtils.copyInputStreamToFile(uploadfile.getInputStream(), new File(savePath + File.separator
					+ originalFilename));
		}
		handle(auction, bindingResult);
		if (bindingResult.hasErrors()) {
			return "auction/auction-form";
		}

		repo.save(auction);
		redirectAttributes.addFlashAttribute("added", true);
		return "redirect:/auctions";
	}

	@ResponseBody
	@RequestMapping(value = "/stream", method = RequestMethod.GET)
	public SseEmitter stream() {
		ssEmitter = new SseEmitter();
		return ssEmitter;
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
