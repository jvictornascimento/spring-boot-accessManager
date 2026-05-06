package com.jvictornascimento.accessmanager.user;

import jakarta.validation.Valid;
import com.jvictornascimento.accessmanager.web.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Usuários", description = "CRUD simples de usuários. A senha recebida no cadastro é armazenada com hash BCrypt.")
@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@Operation(
		summary = "Cadastra um novo usuário",
		description = "Cria um usuário ativo e salva a senha usando hash BCrypt. O hash nunca é retornado na resposta."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Usuário criado",
			content = @Content(schema = @Schema(implementation = UserResponse.class))),
		@ApiResponse(responseCode = "400", description = "Dados inválidos",
			content = @Content(schema = @Schema(implementation = ApiError.class))),
		@ApiResponse(responseCode = "409", description = "Email já cadastrado",
			content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserResponse create(@Valid @RequestBody CreateUserRequest request) {
		return userService.create(request);
	}

	@Operation(summary = "Lista usuários ativos", description = "Retorna apenas usuários ativos.")
	@ApiResponse(responseCode = "200", description = "Lista de usuários ativos",
		content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))))
	@GetMapping
	public List<UserResponse> findAll() {
		return userService.findAllActive();
	}

	@Operation(summary = "Busca usuário ativo por ID", description = "Retorna um usuário ativo pelo identificador.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Usuário encontrado",
			content = @Content(schema = @Schema(implementation = UserResponse.class))),
		@ApiResponse(responseCode = "404", description = "Usuário não encontrado ou inativo",
			content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
	@GetMapping("/{id}")
	public UserResponse findById(@PathVariable Long id) {
		return userService.findActiveById(id);
	}

	@Operation(summary = "Atualiza um usuário ativo", description = "Atualiza nome e email de um usuário ativo.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Usuário atualizado",
			content = @Content(schema = @Schema(implementation = UserResponse.class))),
		@ApiResponse(responseCode = "400", description = "Dados inválidos",
			content = @Content(schema = @Schema(implementation = ApiError.class))),
		@ApiResponse(responseCode = "404", description = "Usuário não encontrado ou inativo",
			content = @Content(schema = @Schema(implementation = ApiError.class))),
		@ApiResponse(responseCode = "409", description = "Email já cadastrado por outro usuário",
			content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
	@PutMapping("/{id}")
	public UserResponse update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
		return userService.update(id, request);
	}

	@Operation(summary = "Desativa um usuário", description = "Realiza exclusão lógica do usuário, mantendo o registro no banco.")
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "Usuário desativado", content = @Content),
		@ApiResponse(responseCode = "404", description = "Usuário não encontrado ou inativo",
			content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		userService.deactivate(id);
	}

}
