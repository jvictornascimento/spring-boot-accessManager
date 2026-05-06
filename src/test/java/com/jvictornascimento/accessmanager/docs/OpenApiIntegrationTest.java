package com.jvictornascimento.accessmanager.docs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@AutoConfigureTestRestTemplate
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OpenApiIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void shouldExposeOpenApiDocumentationWithoutAuthentication() {
		var response = restTemplate.getForEntity("/v3/api-docs", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("\"title\":\"Bear Control Access Weather API\"");
		assertThat(response.getBody()).contains("\"bearerAuth\"");
		assertThat(response.getBody()).contains("\"/api/auth/login\"");
		assertThat(response.getBody()).contains("\"/api/auth/me\"");
		assertThat(response.getBody()).contains("\"/api/users\"");
		assertThat(response.getBody()).contains("\"/api/weather\"");
		assertThat(response.getBody()).contains("\"operationId\":\"login\"");
		assertThat(response.getBody()).contains("\"operationId\":\"findByCity\"");
	}

	@Test
	void shouldExposeSwaggerUiWithoutAuthentication() {
		var response = restTemplate.getForEntity("/swagger-ui/index.html", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

}
