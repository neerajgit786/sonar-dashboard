package com.dashboard.app.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dashboard.app.entity.UserDto;

public interface UserRepository extends JpaRepository<UserDto, Long> {

	Optional<UserDto> findByUsername(String username);
	Optional<UserDto> findByEmail(String email);


}