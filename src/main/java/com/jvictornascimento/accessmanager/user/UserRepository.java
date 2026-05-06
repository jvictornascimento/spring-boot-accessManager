package com.jvictornascimento.accessmanager.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findAllByActiveTrueOrderByNameAsc();

	Optional<User> findByIdAndActiveTrue(Long id);

	Optional<User> findByEmailAndActiveTrue(String email);

	boolean existsByEmail(String email);

	boolean existsByEmailAndIdNot(String email, Long id);

}
