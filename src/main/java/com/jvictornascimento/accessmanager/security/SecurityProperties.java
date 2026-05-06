package com.jvictornascimento.accessmanager.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security")
public record SecurityProperties(
	Jwt jwt,
	RefreshToken refreshToken
) {

	public record Jwt(
		String secret,
		long accessTokenExpirationMinutes
	) {
	}

	public record RefreshToken(
		long expirationDays,
		boolean cookieSecure,
		String cookieSameSite
	) {
	}

}
