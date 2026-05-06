package com.jvictornascimento.accessmanager.auth;

import com.jvictornascimento.accessmanager.user.UserRepository;
import com.jvictornascimento.accessmanager.user.UserResponse;
import com.jvictornascimento.accessmanager.web.InvalidCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional(readOnly = true)
	public LoginResponse login(LoginRequest request) {
		var user = userRepository.findByEmailAndActiveTrue(request.email().trim().toLowerCase())
			.orElseThrow(InvalidCredentialsException::new);

		if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
			throw new InvalidCredentialsException();
		}

		return new LoginResponse(true, UserResponse.from(user));
	}

}
