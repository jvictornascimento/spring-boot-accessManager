package com.jvictornascimento.accessmanager.auth;

import com.jvictornascimento.accessmanager.user.UserResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de autenticação com access token e dados públicos do usuário.")
public record LoginResponse(
	@Schema(description = "Indica se a autenticação foi concluída.", example = "true")
	boolean authenticated,

	@Schema(description = "JWT usado no header Authorization como Bearer token.", example = "eyJhbGciOiJIUzI1NiJ9...")
	String accessToken,

	@Schema(description = "Dados públicos do usuário autenticado.")
	UserResponse user
) {
}
