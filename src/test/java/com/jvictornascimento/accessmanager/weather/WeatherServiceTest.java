package com.jvictornascimento.accessmanager.weather;

import com.jvictornascimento.accessmanager.web.ExternalServiceException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WeatherServiceTest {

	@Test
	void shouldMapOpenWeatherResponse() {
		var service = new WeatherService(new FakeOpenWeatherClient(), new OpenWeatherProperties("http://localhost", "api-key"));

		var response = service.findByCity("Recife");

		assertThat(response.city()).isEqualTo("Recife");
		assertThat(response.country()).isEqualTo("BR");
		assertThat(response.temperature()).isEqualTo(30.5);
		assertThat(response.feelsLike()).isEqualTo(32.0);
		assertThat(response.description()).isEqualTo("céu limpo");
	}

	@Test
	void shouldFailWhenApiKeyIsMissing() {
		var service = new WeatherService(new FakeOpenWeatherClient(), new OpenWeatherProperties("http://localhost", ""));

		assertThatThrownBy(() -> service.findByCity("Recife"))
			.isInstanceOf(ExternalServiceException.class)
			.hasMessage("OpenWeather API key is not configured");
	}

	private static class FakeOpenWeatherClient implements OpenWeatherClient {

		@Override
		public OpenWeatherCurrentResponse getCurrentWeather(String city, String apiKey, String units, String lang) {
			return new OpenWeatherCurrentResponse(
				city,
				new OpenWeatherCurrentResponse.Sys("BR"),
				new OpenWeatherCurrentResponse.Main(30.5, 32.0, 70, 1012),
				List.of(new OpenWeatherCurrentResponse.Weather("Clear", "céu limpo", "01d")),
				new OpenWeatherCurrentResponse.Wind(4.1)
			);
		}

	}

}
