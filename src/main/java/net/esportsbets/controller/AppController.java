package net.esportsbets.controller;

import java.util.List;

import net.esportsbets.dao.User;
import net.esportsbets.repository.UserRepository;
import net.esportsbets.service.BetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AppController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private BetsService betsService;
	
	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}

	@GetMapping("/login")
	public String viewLoginPage() {

		return "login";
	}
	
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		
		return "signup_form";
	}
	
	@PostMapping("/process_register")
	public String processRegister(User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		
		betsService.createUserCredit(userRepo.save(user), 2000.0, null, "new user bonus credit");
		
		return "register_success";
	}
	
	@GetMapping("/users")
	public String listUsers(Model model) {
		List<User> listUsers = userRepo.findAll();
		model.addAttribute("listUsers", listUsers);
		
		return "users";
	}

	@GetMapping("/home")
	public String testHome() { return "home_page"; }

	@GetMapping("/players")
    public String viewPlayerStatisticsPage() { return "players"; }

	@GetMapping("/results")
	public String viewResultsPage() { return "results"; }

    @GetMapping("/history")
    public String viewHistoryPage() { return "history"; }

}
