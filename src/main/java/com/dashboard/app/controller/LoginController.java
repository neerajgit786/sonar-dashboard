package com.dashboard.app.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}

	@GetMapping("/dashboard")
	public String homePage(Authentication authentication, Model model) {
		String username = authentication.getName();
		model.addAttribute("username", username);
		return "reports";
	}

}
