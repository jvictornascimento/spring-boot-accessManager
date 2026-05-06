package com.jvictornascimento.accessmanager.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String BEARER_PREFIX = "Bearer ";

	private final TokenService tokenService;

	public JwtAuthenticationFilter(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authorization != null && authorization.startsWith(BEARER_PREFIX)) {
			authenticate(authorization.substring(BEARER_PREFIX.length()));
		}

		filterChain.doFilter(request, response);
	}

	private void authenticate(String token) {
		try {
			if (!tokenService.isAccessToken(token)) {
				return;
			}
			var email = tokenService.extractSubject(token);
			var authentication = new UsernamePasswordAuthenticationToken(email, null, List.of());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		catch (JwtException | IllegalArgumentException exception) {
			SecurityContextHolder.clearContext();
		}
	}

}
