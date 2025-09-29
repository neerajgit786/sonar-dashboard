package com.dashboard.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.dashboard.app.entity.UserDto;
import com.dashboard.app.model.User;
import com.dashboard.app.service.impl.EmailServiceBoot;
import com.dashboard.app.service.impl.UserService;

import jakarta.validation.Valid;

@Controller
public class UserController {

	@Autowired
	UserService service;
	@Autowired
	EmailServiceBoot emailService;

	@GetMapping("/signup")
	public String signupPage(Model model) {

		model.addAttribute("user", new User());
		return "signup";
	}

	@GetMapping("/forgot-password")
	public String forgotPwdPage(Model model) {
		model.addAttribute("user", new User());
		return "forgot-password";
	}

	@PostMapping("/doSignup")
	public String handleSignup(@Valid @ModelAttribute("user") User user, Model model) {

		if (!user.getPassword().equals(user.getConfirmPassword())) {
			model.addAttribute("error", "Passwords do not match!");
			return "signup";
		}
		UserDto userDto = service.getUserByEmail(user.getEmail());
		if (userDto != null) {
			model.addAttribute("error", "email already exists!");
			return "signup";
		}

		System.out.println("New user signed up: " + user.getUsername() + " (" + user.getEmail() + ")");

		try {
			service.createUser(user);
			System.out.println("user successfully created");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("user creation Failed!");
			return "signup";
		}

		return "redirect:/login?signupSuccess";
	}

	@PostMapping("/doForgotPassword")
	public String handleForgotPwd(@Valid String email, Model model) {

		if (email == null) {
			return "forgot-password";
		}
		UserDto userDto = service.getUserByEmail(email);
		if (userDto == null) {
			model.addAttribute("errorMessage", "error: email do not exists.");
			return "forgot-password";
		}
		System.out.println("Reset Password : (" + email + ")");

		try {
			emailService.sendResetEmail(email, "token:78934e");
			System.out.println("email sent successfully");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("email sent failed");
			model.addAttribute("errorMessage", "error: email sent failure. ("+e.getMessage()+")");
			return "forgot-password";
		}

		return "redirect:/login?resetSuccess";
	}
}
