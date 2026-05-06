package com.jvictornascimento.accessmanager.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados permitidos para atualização de usuário.")
public record UpdateUserRequest(
	@Schema(description = "Nome completo atualizado.", example = "Operador Bear Flow", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message = "Name is required")
	@Size(max = 120, message = "Name must have at most 120 characters")
	String name,

	@Schema(description = "Email atualizado e único.", example = "operador@bearflow.local", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message = "Email is required")
	@Email(message = "Email must be valid")
	@Size(max = 160, message = "Email must have at most 160 characters")
	String email
) {
}
