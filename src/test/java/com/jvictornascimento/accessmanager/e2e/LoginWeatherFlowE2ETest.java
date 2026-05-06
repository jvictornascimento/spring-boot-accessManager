package com.jvictornascimento.accessmanager.e2e;

import com.jvictornascimento.accessmanager.auth.LoginResponse;
import com.jvictornascimento.accessmanager.weather.OpenWeatherClient;
import com.jvictornascimento.accessmanager.weather.OpenWeatherCurrentResponse;
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
class LoginWeatherFlowE2ETest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void shouldLoginAndConsumeWeatherAsAuthenticatedUser() {
		var login = restTemplate.postForEntity(
			"/api/auth/login",
			Map.of("email", "admin@bearflow.local", "password", "password"),
			LoginResponse.class
		);

		assertThat(login.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(login.getBody().accessToken()).isNotBlank();
		assertThat(login.getHeaders().get(HttpHeaders.SET_COOKIE))
			.anySatisfy(cookie -> assertThat(cookie).contains("refresh_token=").contains("HttpOnly"));

		var headers = new HttpHeaders();
		headers.setBearerAuth(login.getBody().accessToken());
		var weather = restTemplate.exchange(
			"/api/weather?city=Recife",
			HttpMethod.GET,
			new HttpEntity<>(headers),
			String.class
		);

		assertThat(weather.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(weather.getBody()).contains("Recife", "céu limpo");
	}

	@TestConfiguration
	static class E2EWeatherTestConfig {

		@Bean
		@Primary
		OpenWeatherClient openWeatherClient() {
			return (city, apiKey, units, lang) -> new OpenWeatherCurrentResponse(
				city,
				new OpenWeatherCurrentResponse.Sys("BR"),
				new OpenWeatherCurrentResponse.Main(29.0, 31.0, 78, 1011),
				List.of(new OpenWeatherCurrentResponse.Weather("Clear", "céu limpo", "01d")),
				new OpenWeatherCurrentResponse.Wind(3.5)
			);
		}

	}

}
