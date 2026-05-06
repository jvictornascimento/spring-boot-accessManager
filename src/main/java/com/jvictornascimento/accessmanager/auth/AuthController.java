package com.jvictornascimento.accessmanager.auth;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import com.jvictornascimento.accessmanager.security.SecurityProperties;
import com.jvictornascimento.accessmanager.user.UserResponse;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;
	private final SecurityProperties securityProperties;

	public AuthController(AuthService authService, SecurityProperties securityProperties) {
		this.authService = authService;
		this.securityProperties = securityProperties;
	}

	@PostMapping("/login")
	public LoginResponse login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
		var loginResponse = authService.login(request);
		var refreshToken = authService.generateRefreshToken(loginResponse.user().email());
		var cookie = ResponseCookie.from("refresh_token", refreshToken)
			.httpOnly(true)
			.secure(securityProperties.refreshToken().cookieSecure())
			.sameSite(securityProperties.refreshToken().cookieSameSite())
			.path("/api/auth")
			.maxAge(Duration.ofDays(securityProperties.refreshToken().expirationDays()))
			.build();
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

		return loginResponse;
	}

	@GetMapping("/me")
	public UserResponse me(Authentication authentication) {
		return authService.findAuthenticatedUser(authentication.getName());
	}

}
