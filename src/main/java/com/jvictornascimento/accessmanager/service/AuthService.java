package com.jvictornascimento.accessmanager.service;

import com.jvictornascimento.accessmanager.dto.in.LoginRequestDTO;
import com.jvictornascimento.accessmanager.dto.out.LoginResponseDTO;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO requestDTO, HttpServletResponse response);
}
