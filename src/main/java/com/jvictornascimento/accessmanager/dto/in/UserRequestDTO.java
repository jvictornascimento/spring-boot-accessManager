package com.jvictornascimento.accessmanager.dto.in;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.time.LocalDate;

import static com.jvictornascimento.accessmanager.service.exceptions.BaseMessageError.*;

public record UserRequestDTO(
        @NotBlank(message = "{user.requiredField.name}")
        String name,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate birthdate,
        @NotBlank(message = "{user.requiredField.email}")
        @Email(message = "{user.email.valid}")
        String email,
        @NotBlank(message = "{user.requiredField.password}")
        String password) {
}
