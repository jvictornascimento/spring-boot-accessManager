package com.jvictornascimento.accessmanager.security;

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

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@AutoConfigureTestRestTemplate
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CorsIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void shouldAllowPreflightFromConfiguredOrigin() {
		var headers = new HttpHeaders();
		headers.setOrigin("http://localhost:3000");
		headers.add(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET");
		headers.add(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "authorization");

		var response = restTemplate.exchange("/api/weather?city=Recife", HttpMethod.OPTIONS, new HttpEntity<>(headers), String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getHeaders().getFirst(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN))
			.isEqualTo("http://localhost:3000");
		assertThat(response.getHeaders().getFirst(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS))
			.isEqualTo("true");
		assertThat(response.getHeaders().getFirst(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS))
			.containsIgnoringCase("authorization");
	}

	@Test
	void shouldRejectPreflightFromUnknownOrigin() {
		var headers = new HttpHeaders();
		headers.setOrigin("https://unknown.example");
		headers.add(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET");

		var response = restTemplate.exchange("/api/weather?city=Recife", HttpMethod.OPTIONS, new HttpEntity<>(headers), String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		assertThat(response.getHeaders().getFirst(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)).isNull();
	}

}
