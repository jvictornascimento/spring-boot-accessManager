package com.jvictornascimento.accessmanager.dto.mapper;

import com.jvictornascimento.accessmanager.dto.in.UserRequestDTO;
import com.jvictornascimento.accessmanager.dto.out.UserResponseDTO;
import com.jvictornascimento.accessmanager.model.User;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface UserMapper {
    UserResponseDTO fromOut(User user);
    User fromEntity(UserRequestDTO userRequestDTO);

}
