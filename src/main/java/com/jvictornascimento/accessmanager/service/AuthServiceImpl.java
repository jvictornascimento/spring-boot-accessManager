package com.jvictornascimento.accessmanager.service;

import com.jvictornascimento.accessmanager.dto.in.LoginRequestDTO;
import com.jvictornascimento.accessmanager.dto.out.LoginResponseDTO;
import com.jvictornascimento.accessmanager.security.jwt.JwtUtils;
import com.jvictornascimento.accessmanager.service.exceptions.InvalidLoginException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.jvictornascimento.accessmanager.service.exceptions.BaseMessageError.LOGIN_INVALID_REQUEST;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;


    @Override
    public LoginResponseDTO login(LoginRequestDTO requestDTO, HttpServletResponse response){
       try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(requestDTO.email(), requestDTO.password()));
            String accessToken = jwtUtils.generateAccessTokenForUser(authentication);
            Instant expiredAt = jwtUtils.getExpiredAtFromToken(accessToken);
            return new LoginResponseDTO(accessToken, expiredAt);
        }catch (BadCredentialsException e){
           throw new InvalidLoginException(LOGIN_INVALID_REQUEST.getMassage());
       }
    }
}
