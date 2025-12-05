package com.jvictornascimento.accessmanager.dto.in;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record UserUpdateRequestDTO(String name,
                                   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                                   LocalDate birthdate,
                                   String password) {
}
