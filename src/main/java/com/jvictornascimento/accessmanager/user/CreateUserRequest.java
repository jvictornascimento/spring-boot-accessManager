package com.jvictornascimento.accessmanager.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastrar um usuário.")
public record CreateUserRequest(
	@Schema(description = "Nome completo do usuário.", example = "Admin Bear Flow", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message = "Name is required")
	@Size(max = 120, message = "Name must have at most 120 characters")
	String name,

	@Schema(description = "Email único usado para login.", example = "admin@bearflow.local", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message = "Email is required")
	@Email(message = "Email must be valid")
	@Size(max = 160, message = "Email must have at most 160 characters")
	String email,

	@Schema(description = "Senha do usuário. Será armazenada somente como hash BCrypt.", example = "password", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message = "Password is required")
	@Size(min = 8, max = 100, message = "Password must have between 8 and 100 characters")
	String password
) {
}
