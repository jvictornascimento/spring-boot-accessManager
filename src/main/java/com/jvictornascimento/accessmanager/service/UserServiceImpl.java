package com.jvictornascimento.accessmanager.service;

import com.jvictornascimento.accessmanager.dto.in.UserRequestDTO;
import com.jvictornascimento.accessmanager.dto.in.UserUpdateRequestDTO;
import com.jvictornascimento.accessmanager.dto.mapper.UserMapper;
import com.jvictornascimento.accessmanager.dto.out.UserResponseDTO;
import com.jvictornascimento.accessmanager.repository.UserRepository;
import com.jvictornascimento.accessmanager.service.exceptions.EmailAlreadyExistsException;
import com.jvictornascimento.accessmanager.service.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.jvictornascimento.accessmanager.service.exceptions.BaseMessageError.USER_EMAIL_ALREADY_EXISTS;
import static com.jvictornascimento.accessmanager.service.exceptions.BaseMessageError.USER_NOT_FOUND_BY_ID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;
    @Override
    public UserResponseDTO getUserById(Long id) {
        var user = userRepository.findById(id).orElseThrow(
                ()-> new UserNotFoundException(USER_NOT_FOUND_BY_ID.params(String.valueOf(id)).getMassage()));
        return mapper.fromOut(user);
    }

    @Override
    public List<UserResponseDTO> getAll() {
        return userRepository.findAll().stream().distinct()
                .map(mapper::fromOut)
                .toList();
    }
    @Override
    public UserResponseDTO insertUser(UserRequestDTO requestDTO) {
        var newUser = Optional.of(requestDTO)
                .filter(user -> !userRepository.existsByEmail(requestDTO.email()))
                        .map(request -> userRepository.save(mapper.fromEntity(request)))
                                .orElseThrow(()-> new EmailAlreadyExistsException(USER_EMAIL_ALREADY_EXISTS.getMassage()));
        return mapper.fromOut(newUser);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserUpdateRequestDTO updateRequestDTO) {
        var user = userRepository.findById(id).orElseThrow(
                ()-> new UserNotFoundException(USER_NOT_FOUND_BY_ID.params(String.valueOf(id)).getMassage()));
        user.setName(updateRequestDTO.name() != null ? updateRequestDTO.name() : user.getName());
        user.setBirthdate(updateRequestDTO.birthdate() != null ? updateRequestDTO.birthdate() : user.getBirthdate());
        user.setPassword(updateRequestDTO.password() != null ? updateRequestDTO.password() : user.getPassword());
        return mapper.fromOut(userRepository.save(user));
    }
    @Override
    public void deleteUser(Long id) {
        var user = userRepository.findById(id).orElseThrow(
                ()-> new UserNotFoundException(USER_NOT_FOUND_BY_ID.params(String.valueOf(id)).getMassage()));
        userRepository.delete(user);
    }
}
