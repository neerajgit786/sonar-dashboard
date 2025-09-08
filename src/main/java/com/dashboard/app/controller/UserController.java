package com.dashboard.app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.dashboard.app.entity.User;
import com.dashboard.app.service.impl.UserService;

@Controller
public class UserController {
	
	@Autowired
	UserService service;
	

	@GetMapping("/signup")
    public String signupPage() {
        return "signup"; // resolves to signup.html (Thymeleaf or JSP)
    }
	@PostMapping("/doSignup")
    public String handleSignup(@ModelAttribute User user,Model model) {
        // ✅ Basic validation
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("error","Passwords do not match!");
            return "signup";
        }

        System.out.println("New user signed up: " + user.getUsername() + " (" + user.getEmail() + ")");
        try {
        service.createUser(user);
        } 
        catch(Exception e) {
        	e.printStackTrace();
        	return "signup";
        }
        
        return "redirect:/login?signupSuccess";
    }
}
