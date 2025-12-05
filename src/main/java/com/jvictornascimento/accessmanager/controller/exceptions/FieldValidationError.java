package com.jvictornascimento.accessmanager.controller.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.util.Map;

public record FieldValidationError(
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",timezone = "GTM")
    Instant timestamp,
    Integer status,
    String error,
    Map<String, String> message,
    String path){
}
