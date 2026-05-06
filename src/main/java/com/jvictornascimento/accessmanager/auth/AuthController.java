package com.jvictornascimento.accessmanager.auth;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import com.jvictornascimento.accessmanager.security.SecurityProperties;
import com.jvictornascimento.accessmanager.user.UserResponse;

import java.time.Duration;

@Tag(name = "Autenticação", description = "Login JWT, refresh token via cookie HTTP-only e dados do usuário autenticado.")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;
	private final SecurityProperties securityProperties;

	public AuthController(AuthService authService, SecurityProperties securityProperties) {
		this.authService = authService;
		this.securityProperties = securityProperties;
	}

	@Operation(
		summary = "Autentica um usuário",
		description = "Valida email e senha, retorna um access token JWT e envia o refresh token no cookie `refresh_token`."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
			content = @Content(schema = @Schema(implementation = LoginResponse.class))),
		@ApiResponse(responseCode = "400", description = "Payload inválido",
			content = @Content(examples = @ExampleObject(value = """
				{
				  "timestamp": "2026-05-06T19:00:00Z",
				  "status": 400,
				  "error": "Bad Request",
				  "message": "Validation failed",
				  "details": ["email: Email must be valid"]
				}
				"""))),
		@ApiResponse(responseCode = "401", description = "Credenciais inválidas",
			content = @Content(examples = @ExampleObject(value = """
				{
				  "timestamp": "2026-05-06T19:00:00Z",
				  "status": 401,
				  "error": "Unauthorized",
				  "message": "Invalid credentials",
				  "details": []
				}
				""")))
	})
	@PostMapping("/login")
	public LoginResponse login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
		var loginResponse = authService.login(request);
		var refreshToken = authService.generateRefreshToken(loginResponse.user().email());
		var cookie = ResponseCookie.from("refresh_token", refreshToken)
			.httpOnly(true)
			.secure(securityProperties.refreshToken().cookieSecure())
			.sameSite(securityProperties.refreshToken().cookieSameSite())
			.path("/api/auth")
			.maxAge(Duration.ofDays(securityProperties.refreshToken().expirationDays()))
			.build();
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

		return loginResponse;
	}

	@Operation(
		summary = "Consulta o usuário autenticado",
		description = "Retorna os dados públicos do usuário vinculado ao JWT informado.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Usuário autenticado encontrado",
			content = @Content(schema = @Schema(implementation = UserResponse.class))),
		@ApiResponse(responseCode = "401", description = "JWT ausente, inválido ou expirado", content = @Content)
	})
	@GetMapping("/me")
	public UserResponse me(Authentication authentication) {
		return authService.findAuthenticatedUser(authentication.getName());
	}

}
