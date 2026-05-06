package com.jvictornascimento.accessmanager.weather;

import com.jvictornascimento.accessmanager.web.ExternalServiceException;
import com.jvictornascimento.accessmanager.web.ResourceNotFoundException;
import feign.FeignException;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

	private static final String UNITS = "metric";
	private static final String LANGUAGE = "pt_br";

	private final OpenWeatherClient openWeatherClient;
	private final OpenWeatherProperties openWeatherProperties;

	public WeatherService(OpenWeatherClient openWeatherClient, OpenWeatherProperties openWeatherProperties) {
		this.openWeatherClient = openWeatherClient;
		this.openWeatherProperties = openWeatherProperties;
	}

	public WeatherResponse findByCity(String city) {
		if (openWeatherProperties.key() == null || openWeatherProperties.key().isBlank()) {
			throw new ExternalServiceException("OpenWeather API key is not configured");
		}

		try {
			return map(openWeatherClient.getCurrentWeather(city.trim(), openWeatherProperties.key(), UNITS, LANGUAGE));
		}
		catch (FeignException.NotFound exception) {
			throw new ResourceNotFoundException("City not found");
		}
		catch (FeignException.Unauthorized exception) {
			throw new ExternalServiceException("OpenWeather API key is invalid");
		}
		catch (FeignException exception) {
			throw new ExternalServiceException("OpenWeather service is unavailable");
		}
	}

	private WeatherResponse map(OpenWeatherCurrentResponse response) {
		OpenWeatherCurrentResponse.Weather weather = response.weather() == null || response.weather().isEmpty()
			? null
			: response.weather().get(0);

		return new WeatherResponse(
			response.name(),
			response.sys() == null ? null : response.sys().country(),
			response.main().temp(),
			response.main().feels_like(),
			response.main().humidity(),
			response.main().pressure(),
			response.wind() == null ? 0 : response.wind().speed(),
			weather == null ? null : weather.main(),
			weather == null ? null : weather.description(),
			weather == null ? null : weather.icon()
		);
	}

}
