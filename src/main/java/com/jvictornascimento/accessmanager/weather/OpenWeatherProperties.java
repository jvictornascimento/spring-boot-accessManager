package com.jvictornascimento.accessmanager.weather;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "openweather.api")
public record OpenWeatherProperties(
	String url,
	String key
) {
}
