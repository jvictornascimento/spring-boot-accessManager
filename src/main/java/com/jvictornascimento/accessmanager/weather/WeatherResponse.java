package com.jvictornascimento.accessmanager.weather;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Clima atual normalizado a partir da OpenWeather.")
public record WeatherResponse(
	@Schema(description = "Cidade consultada.", example = "Recife")
	String city,

	@Schema(description = "Código ISO do país retornado pela OpenWeather.", example = "BR")
	String country,

	@Schema(description = "Temperatura atual em Celsius.", example = "30.5")
	double temperature,

	@Schema(description = "Sensação térmica em Celsius.", example = "32.0")
	double feelsLike,

	@Schema(description = "Umidade relativa do ar em percentual.", example = "70")
	int humidity,

	@Schema(description = "Pressão atmosférica em hPa.", example = "1012")
	int pressure,

	@Schema(description = "Velocidade do vento em m/s.", example = "4.1")
	double windSpeed,

	@Schema(description = "Condição principal retornada pela OpenWeather.", example = "Clear")
	String condition,

	@Schema(description = "Descrição localizada em português.", example = "céu limpo")
	String description,

	@Schema(description = "Código do ícone da OpenWeather.", example = "01d")
	String icon
) {
}
