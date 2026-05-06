package com.jvictornascimento.accessmanager.weather;

import com.jvictornascimento.accessmanager.auth.LoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@AutoConfigureTestRestTemplate
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WeatherControllerIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void shouldRejectWeatherRequestWithoutJwt() {
		var response = restTemplate.getForEntity("/api/weather?city=Recife", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	void shouldReturnWeatherForAuthenticatedUser() {
		var headers = authenticatedHeaders();

		var response = restTemplate.exchange(
			"/api/weather?city=Recife",
			HttpMethod.GET,
			new HttpEntity<>(headers),
			String.class
		);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("Recife");
		assertThat(response.getBody()).contains("céu limpo");
	}

	private HttpHeaders authenticatedHeaders() {
		var login = restTemplate.postForEntity(
			"/api/auth/login",
			Map.of("email", "admin@bearflow.local", "password", "password"),
			LoginResponse.class
		);
		var headers = new HttpHeaders();
		headers.setBearerAuth(login.getBody().accessToken());
		return headers;
	}

	@TestConfiguration
	static class WeatherTestConfig {

		@Bean
		@Primary
		OpenWeatherClient openWeatherClient() {
			return (city, apiKey, units, lang) -> new OpenWeatherCurrentResponse(
				city,
				new OpenWeatherCurrentResponse.Sys("BR"),
				new OpenWeatherCurrentResponse.Main(30.5, 32.0, 70, 1012),
				List.of(new OpenWeatherCurrentResponse.Weather("Clear", "céu limpo", "01d")),
				new OpenWeatherCurrentResponse.Wind(4.1)
			);
		}

	}

}
