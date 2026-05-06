package com.jvictornascimento.accessmanager.weather;

import java.util.List;

public record OpenWeatherCurrentResponse(
	String name,
	Sys sys,
	Main main,
	List<Weather> weather,
	Wind wind
) {

	public record Sys(String country) {
	}

	public record Main(
		double temp,
		double feels_like,
		int humidity,
		int pressure
	) {
	}

	public record Weather(
		String main,
		String description,
		String icon
	) {
	}

	public record Wind(double speed) {
	}

}
