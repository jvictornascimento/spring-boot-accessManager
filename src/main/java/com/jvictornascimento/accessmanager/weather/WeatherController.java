package com.jvictornascimento.accessmanager.weather;

import jakarta.validation.constraints.NotBlank;
import com.jvictornascimento.accessmanager.web.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Clima", description = "Consulta de clima atual por cidade consumindo a API OpenWeather via Spring OpenFeign.")
@Validated
@RestController
@RequestMapping("/api/weather")
public class WeatherController {

	private final WeatherService weatherService;

	public WeatherController(WeatherService weatherService) {
		this.weatherService = weatherService;
	}

	@Operation(
		summary = "Consulta clima atual por cidade",
		description = "Consome a API OpenWeather e normaliza a resposta para o contrato da Bear Flow.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Clima encontrado",
			content = @Content(schema = @Schema(implementation = WeatherResponse.class),
				examples = @ExampleObject(value = """
					{
					  "city": "Recife",
					  "country": "BR",
					  "temperature": 30.5,
					  "feelsLike": 32.0,
					  "humidity": 70,
					  "pressure": 1012,
					  "windSpeed": 4.1,
					  "condition": "Clear",
					  "description": "céu limpo",
					  "icon": "01d"
					}
					"""))),
		@ApiResponse(responseCode = "400", description = "Cidade ausente ou inválida",
			content = @Content(schema = @Schema(implementation = ApiError.class))),
		@ApiResponse(responseCode = "401", description = "JWT ausente, inválido ou expirado", content = @Content),
		@ApiResponse(responseCode = "502", description = "Falha ao consultar a OpenWeather",
			content = @Content(schema = @Schema(implementation = ApiError.class)))
	})
	@GetMapping
	public WeatherResponse findByCity(
			@Parameter(description = "Nome da cidade consultada na OpenWeather", example = "Recife", required = true)
			@RequestParam
			@NotBlank(message = "City is required")
			String city
	) {
		return weatherService.findByCity(city);
	}

}
