package com.jvictornascimento.accessmanager.auth;

import com.jvictornascimento.accessmanager.user.UserRepository;
import com.jvictornascimento.accessmanager.user.UserResponse;
import com.jvictornascimento.accessmanager.web.ResourceNotFoundException;
import com.jvictornascimento.accessmanager.security.TokenPair;
import com.jvictornascimento.accessmanager.security.TokenService;
import com.jvictornascimento.accessmanager.web.InvalidCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenService tokenService;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.tokenService = tokenService;
	}

	@Transactional(readOnly = true)
	public LoginResponse login(LoginRequest request) {
		var user = userRepository.findByEmailAndActiveTrue(request.email().trim().toLowerCase())
			.orElseThrow(InvalidCredentialsException::new);

		if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
			throw new InvalidCredentialsException();
		}

		var tokens = tokenService.generateTokens(user);

		return new LoginResponse(true, tokens.accessToken(), UserResponse.from(user));
	}

	@Transactional(readOnly = true)
	public UserResponse findAuthenticatedUser(String email) {
		return userRepository.findByEmailAndActiveTrue(email)
			.map(UserResponse::from)
			.orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	public String generateRefreshToken(String email) {
		return tokenService.generateRefreshToken(email);
	}

}
