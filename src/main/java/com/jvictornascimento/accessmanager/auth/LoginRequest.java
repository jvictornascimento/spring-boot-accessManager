package com.jvictornascimento.accessmanager.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciais usadas para autenticação.")
public record LoginRequest(
	@Schema(description = "Email cadastrado do usuário.", example = "admin@bearflow.local", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message = "Email is required")
	@Email(message = "Email must be valid")
	String email,

	@Schema(description = "Senha em texto puro enviada exclusivamente via HTTPS.", example = "password", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message = "Password is required")
	String password
) {
}
