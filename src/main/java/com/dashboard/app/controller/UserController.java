package com.dashboard.app.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.dashboard.app.model.UserDto;

@Controller
public class UserController {

	@GetMapping("/signup")
    public String signupPage() {
        return "signup"; // resolves to signup.html (Thymeleaf or JSP)
    }
	@PostMapping("/doSignup")
    public String handleSignup(@ModelAttribute UserDto user,Model model) {
        // ✅ Basic validation
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("error","Passwords do not match!");
        	// Redirect back to signup with error param
            return "signup";
        }

        // ✅ Here you would save the user to database (JPA / JDBC / service call)
        // Example: userService.save(user);

        System.out.println("New user signed up: " + user.getUsername() + " (" + user.getEmail() + ")");

        // ✅ Redirect to login page with success message
        return "redirect:/login?signupSuccess";
    }
}
