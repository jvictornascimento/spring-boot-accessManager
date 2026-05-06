package com.jvictornascimento.accessmanager.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Dados públicos de um usuário. Não expõe senha nem hash.")
public record UserResponse(
	@Schema(description = "Identificador do usuário.", example = "1")
	Long id,

	@Schema(description = "Nome completo.", example = "Admin Bear Flow")
	String name,

	@Schema(description = "Email usado para login.", example = "admin@bearflow.local")
	String email,

	@Schema(description = "Indica se o usuário está ativo.", example = "true")
	boolean active,

	@Schema(description = "Data de criação do registro.", example = "2026-05-06T19:00:00")
	LocalDateTime createdAt,

	@Schema(description = "Data da última atualização do registro.", example = "2026-05-06T19:30:00")
	LocalDateTime updatedAt
) {

	public static UserResponse from(User user) {
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
