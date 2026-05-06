package com.jvictornascimento.accessmanager.security;

import com.jvictornascimento.accessmanager.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Service
public class TokenService {

	private final SecurityProperties securityProperties;
	private final Clock clock;
	private final SecretKey secretKey;

	public TokenService(SecurityProperties securityProperties, Clock clock) {
		this.securityProperties = securityProperties;
		this.clock = clock;
		this.secretKey = Keys.hmacShaKeyFor(securityProperties.jwt().secret().getBytes(StandardCharsets.UTF_8));
	}

	public TokenPair generateTokens(User user) {
		return new TokenPair(generateAccessToken(user), generateRefreshToken(user.getEmail()));
	}

	public String generateRefreshToken(String email) {
		var now = Instant.now(clock);
		var expiresAt = now.plus(securityProperties.refreshToken().expirationDays(), ChronoUnit.DAYS);

		return Jwts.builder()
			.subject(email)
			.claim("type", "refresh")
			.issuedAt(Date.from(now))
			.expiration(Date.from(expiresAt))
			.signWith(secretKey)
			.compact();
	}

	public String extractSubject(String token) {
		return parseClaims(token).getSubject();
	}

	public boolean isAccessToken(String token) {
		return "access".equals(parseClaims(token).get("type", String.class));
	}

	private String generateAccessToken(User user) {
		var now = Instant.now(clock);
		var expiresAt = now.plus(securityProperties.jwt().accessTokenExpirationMinutes(), ChronoUnit.MINUTES);

		return Jwts.builder()
			.subject(user.getEmail())
			.claims(Map.of("userId", user.getId(), "type", "access"))
			.issuedAt(Date.from(now))
			.expiration(Date.from(expiresAt))
			.signWith(secretKey)
			.compact();
	}

	private Claims parseClaims(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}

}
