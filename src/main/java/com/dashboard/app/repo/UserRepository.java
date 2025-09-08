package com.dashboard.app.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dashboard.app.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);
//    History findTopByMasterIdOrderByUpdatedDateDesc(Long masterId);


}