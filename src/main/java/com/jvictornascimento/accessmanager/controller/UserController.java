package com.jvictornascimento.accessmanager.controller;

import com.jvictornascimento.accessmanager.dto.in.UserRequestDTO;
import com.jvictornascimento.accessmanager.dto.in.UserUpdateRequestDTO;
import com.jvictornascimento.accessmanager.dto.out.UserResponseDTO;
import com.jvictornascimento.accessmanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(Long id) {
        return ResponseEntity.ok(service.getAll());
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getUserById(id));
    }
    @PostMapping
    public ResponseEntity<UserResponseDTO> saveUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        var newData = service.insertUser(userRequestDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newData.id()).toUri();
        return ResponseEntity.created(uri).body(newData);
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequestDTO userUpdateRequestDTO) {
        return ResponseEntity.ok(service.updateUser(id,userUpdateRequestDTO));
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
