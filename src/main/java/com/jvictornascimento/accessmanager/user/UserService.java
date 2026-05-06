package com.jvictornascimento.accessmanager.user;

import com.jvictornascimento.accessmanager.web.EmailAlreadyRegisteredException;
import com.jvictornascimento.accessmanager.web.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final Clock clock;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, Clock clock) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.clock = clock;
	}

	@Transactional
	public UserResponse create(CreateUserRequest request) {
		var email = normalizeEmail(request.email());
		if (userRepository.existsByEmail(email)) {
			throw new EmailAlreadyRegisteredException();
		}

		var now = LocalDateTime.now(clock);
		var user = new User();
		user.setName(request.name().trim());
		user.setEmail(email);
		user.setPasswordHash(passwordEncoder.encode(request.password()));
		user.setActive(true);
		user.setCreatedAt(now);
		user.setUpdatedAt(now);

		return UserResponse.from(userRepository.save(user));
	}

	@Transactional(readOnly = true)
	public List<UserResponse> findAllActive() {
		return userRepository.findAllByActiveTrueOrderByNameAsc()
			.stream()
			.map(UserResponse::from)
			.toList();
	}

	@Transactional(readOnly = true)
	public UserResponse findActiveById(Long id) {
		return UserResponse.from(findUser(id));
	}

	@Transactional
	public UserResponse update(Long id, UpdateUserRequest request) {
		var user = findUser(id);
		var email = normalizeEmail(request.email());
		if (userRepository.existsByEmailAndIdNot(email, id)) {
			throw new EmailAlreadyRegisteredException();
		}

		user.setName(request.name().trim());
		user.setEmail(email);
		user.setUpdatedAt(LocalDateTime.now(clock));

		return UserResponse.from(userRepository.save(user));
	}

	@Transactional
	public void deactivate(Long id) {
		var user = findUser(id);
		user.setActive(false);
		user.setUpdatedAt(LocalDateTime.now(clock));
		userRepository.save(user);
	}

	private User findUser(Long id) {
		return userRepository.findByIdAndActiveTrue(id)
			.orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	private String normalizeEmail(String email) {
		return email.trim().toLowerCase();
	}

}
