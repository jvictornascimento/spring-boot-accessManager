package com.jvictornascimento.accessmanager.controller;

import com.jvictornascimento.accessmanager.dto.in.LoginRequestDTO;
import com.jvictornascimento.accessmanager.dto.out.LoginResponseDTO;
import com.jvictornascimento.accessmanager.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class authController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO requestDTO, HttpServletResponse response) {
        return ResponseEntity.ok(authService.login(requestDTO, response));
    }
}
