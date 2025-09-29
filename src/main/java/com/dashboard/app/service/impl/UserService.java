package com.dashboard.app.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dashboard.app.entity.UserDto;
import com.dashboard.app.model.User;
import com.dashboard.app.repo.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public void createUser(User user) {
		UserDto userdto = new UserDto();
		userdto.setId(user.getId());
		userdto.setUsername(user.getUsername());
		userdto.setEmail(user.getEmail());
		userdto.setPassword(passwordEncoder.encode(user.getPassword()));
		userdto.setRole("USER");
		userRepo.save(userdto);
	}

	public UserDto getUserByEmail(String email) {
		Optional<UserDto> userDto = userRepo.findByEmail(email);
		if (userDto.isPresent()) {
			return userDto.get();
		}
		return null;
	}
}
