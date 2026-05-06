package com.jvictornascimento.accessmanager.weather;

public record WeatherResponse(
	String city,
	String country,
	double temperature,
	double feelsLike,
	int humidity,
	int pressure,
	double windSpeed,
	String condition,
	String description,
	String icon
) {
}
