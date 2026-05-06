package com.jvictornascimento.accessmanager.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
	@NotBlank(message = "Name is required")
	@Size(max = 120, message = "Name must have at most 120 characters")
	String name,

	@NotBlank(message = "Email is required")
	@Email(message = "Email must be valid")
	@Size(max = 160, message = "Email must have at most 160 characters")
	String email,

	@NotBlank(message = "Password is required")
	@Size(min = 8, max = 100, message = "Password must have between 8 and 100 characters")
	String password
) {
}
