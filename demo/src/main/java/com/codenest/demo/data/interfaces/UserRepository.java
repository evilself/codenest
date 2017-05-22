package com.codenest.demo.data.interfaces;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codenest.model.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findById(Long id);
}