package com.jvictornascimento.accessmanager.service;

import com.jvictornascimento.accessmanager.dto.in.UserRequestDTO;
import com.jvictornascimento.accessmanager.dto.in.UserUpdateRequestDTO;
import com.jvictornascimento.accessmanager.dto.out.UserResponseDTO;

import java.util.List;

public interface UserService {
    UserResponseDTO getUserById(Long id);
    List<UserResponseDTO> getAll();
    UserResponseDTO insertUser(UserRequestDTO requestDTO);
    UserResponseDTO updateUser(Long id, UserUpdateRequestDTO updateRequestDTO);
    void deleteUser(Long id);
}
