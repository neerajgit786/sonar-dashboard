package com.dashboard.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dashboard.app.entity.User;
import com.dashboard.app.repo.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public void createUser(User user) {
		
        user.setPassword(passwordEncoder.encode(user.getPassword())); // ✅ encode here
        user.setRole("USER");
		userRepo.save(user);
	}
}
