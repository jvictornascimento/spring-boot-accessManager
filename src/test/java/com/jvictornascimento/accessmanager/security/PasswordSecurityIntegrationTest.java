package com.jvictornascimento.accessmanager.security;

import com.jvictornascimento.accessmanager.user.CreateUserRequest;
import com.jvictornascimento.accessmanager.user.UserRepository;
import com.jvictornascimento.accessmanager.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class PasswordSecurityIntegrationTest {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Test
	void shouldStoreCreatedUserPasswordAsBcryptHash() {
		userService.create(new CreateUserRequest(
			"Password Bear",
			"password.bear@bearflow.local",
			"password123"
		));

		var user = userRepository.findByEmailAndActiveTrue("password.bear@bearflow.local").orElseThrow();

		assertThat(user.getPasswordHash()).startsWith("$2a$");
		assertThat(user.getPasswordHash()).isNotEqualTo("password123");
		assertThat(passwordEncoder.matches("password123", user.getPasswordHash())).isTrue();
		assertThat(passwordEncoder.matches("wrong-password", user.getPasswordHash())).isFalse();
	}

	@Test
	void shouldValidateSeedUserPasswordHash() {
		var user = userRepository.findByEmailAndActiveTrue("admin@bearflow.local").orElseThrow();

		assertThat(user.getPasswordHash()).startsWith("$2a$");
		assertThat(passwordEncoder.matches("password", user.getPasswordHash())).isTrue();
	}

}
