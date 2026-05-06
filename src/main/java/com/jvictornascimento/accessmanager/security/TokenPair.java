package com.jvictornascimento.accessmanager.security;

public record TokenPair(
	String accessToken,
	String refreshToken
) {
}
