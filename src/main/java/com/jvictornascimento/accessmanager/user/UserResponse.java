package com.jvictornascimento.accessmanager.user;

import java.time.LocalDateTime;

public record UserResponse(
	Long id,
	String name,
	String email,
	boolean active,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {

	static UserResponse from(User user) {
		return new UserResponse(
			user.getId(),
			user.getName(),
			user.getEmail(),
			user.isActive(),
			user.getCreatedAt(),
			user.getUpdatedAt()
		);
	}

}
