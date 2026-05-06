package com.jvictornascimento.accessmanager.web;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Schema(description = "Formato padronizado para respostas de erro da API.")
public record ApiError(
	@Schema(description = "Momento em que o erro foi produzido.", example = "2026-05-06T19:00:00Z")
	Instant timestamp,

	@Schema(description = "Status HTTP numérico.", example = "400")
	int status,

	@Schema(description = "Descrição padrão do status HTTP.", example = "Bad Request")
	String error,

	@Schema(description = "Mensagem de negócio ou validação.", example = "Validation failed")
	String message,

	@Schema(description = "Detalhes adicionais, principalmente erros de validação.", example = "[\"email: Email must be valid\"]")
	List<String> details
) {

	public static ApiError of(int status, String error, String message) {
		return new ApiError(Instant.now(), status, error, message, List.of());
	}

	public static ApiError of(int status, String error, String message, List<String> details) {
		return new ApiError(Instant.now(), status, error, message, details);
	}

}
