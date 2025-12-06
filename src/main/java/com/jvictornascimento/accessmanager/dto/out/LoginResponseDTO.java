package com.jvictornascimento.accessmanager.dto.out;

import java.time.Instant;

public record LoginResponseDTO(String token, Instant expiresAt) {
}
