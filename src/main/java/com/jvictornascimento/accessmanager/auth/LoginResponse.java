package com.jvictornascimento.accessmanager.auth;

import com.jvictornascimento.accessmanager.user.UserResponse;

public record LoginResponse(
	boolean authenticated,
	String accessToken,
	UserResponse user
) {
}
