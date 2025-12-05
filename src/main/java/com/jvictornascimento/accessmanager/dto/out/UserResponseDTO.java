package com.jvictornascimento.accessmanager.dto.out;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.time.LocalDate;

public record UserResponseDTO(Long id, String name,
                              @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                              LocalDate birthdate, String email) {
}
