package com.jvictornascimento.accessmanager.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@AutoConfigureTestRestTemplate
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void shouldLoginWithValidCredentials() {
		var request = Map.of("email", "admin@bearflow.local", "password", "password");

		var response = restTemplate.postForEntity("/api/auth/login", request, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("admin@bearflow.local");
		assertThat(response.getBody()).contains("\"authenticated\":true");
		assertThat(response.getBody()).contains("accessToken");
		assertThat(response.getBody()).doesNotContain("password");
		assertThat(response.getBody()).doesNotContain("passwordHash");
		assertThat(response.getHeaders().get(HttpHeaders.SET_COOKIE))
			.anySatisfy(cookie -> assertThat(cookie).contains("refresh_token=").contains("HttpOnly"));
	}

	@Test
	void shouldRejectInvalidPassword() {
		var request = Map.of("email", "admin@bearflow.local", "password", "wrong-password");

		var response = restTemplate.postForEntity("/api/auth/login", request, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		assertThat(response.getBody()).contains("Invalid credentials");
	}

	@Test
	void shouldRejectUnknownEmail() {
		var request = Map.of("email", "missing@bearflow.local", "password", "password");

		var response = restTemplate.postForEntity("/api/auth/login", request, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		assertThat(response.getBody()).contains("Invalid credentials");
	}

	@Test
	void shouldAccessProtectedMeEndpointWithValidJwt() {
		var loginResponse = restTemplate.postForEntity(
			"/api/auth/login",
			Map.of("email", "admin@bearflow.local", "password", "password"),
			LoginResponse.class
		);
		var headers = new HttpHeaders();
		headers.setBearerAuth(loginResponse.getBody().accessToken());

		var response = restTemplate.exchange("/api/auth/me", HttpMethod.GET, new HttpEntity<>(headers), String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("admin@bearflow.local");
	}

	@Test
	void shouldRejectProtectedMeEndpointWithoutJwt() {
		var response = restTemplate.getForEntity("/api/auth/me", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

}
