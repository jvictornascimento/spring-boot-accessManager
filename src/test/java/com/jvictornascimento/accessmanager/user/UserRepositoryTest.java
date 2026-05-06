package com.jvictornascimento.accessmanager.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	void shouldFindActiveUserByEmail() {
		var user = new User();
		user.setName("Jane Bear");
		user.setEmail("jane@bearflow.local");
		user.setPasswordHash("$2a$10$hashhashhashhashhashhashhashhashhashhashhashhashha");
		user.setActive(true);
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdatedAt(LocalDateTime.now());
		userRepository.save(user);

		var foundUser = userRepository.findByEmailAndActiveTrue("jane@bearflow.local");

		assertThat(foundUser).isPresent();
		assertThat(foundUser.get().getName()).isEqualTo("Jane Bear");
	}

	@Test
	void shouldCheckIfEmailExists() {
		assertThat(userRepository.existsByEmail("admin@bearflow.local")).isTrue();
		assertThat(userRepository.existsByEmail("missing@bearflow.local")).isFalse();
	}

}
