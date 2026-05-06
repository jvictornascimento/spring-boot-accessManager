package com.jvictornascimento.accessmanager.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@AutoConfigureTestRestTemplate
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void shouldCreateUserWithoutReturningPassword() {
		var request = Map.of(
			"name", "New Bear",
			"email", "new.bear@bearflow.local",
			"password", "password123"
		);

		var response = restTemplate.postForEntity("/api/users", request, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody()).contains("new.bear@bearflow.local");
		assertThat(response.getBody()).doesNotContain("password123");
		assertThat(response.getBody()).doesNotContain("passwordHash");
	}

	@Test
	void shouldRejectDuplicatedEmail() {
		var request = Map.of(
			"name", "Duplicated Bear",
			"email", "admin@bearflow.local",
			"password", "password123"
		);

		var response = restTemplate.postForEntity("/api/users", request, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
		assertThat(response.getBody()).contains("Email already registered");
	}

	@Test
	void shouldRejectInvalidPayload() {
		var request = Map.of(
			"name", "",
			"email", "invalid",
			"password", ""
		);

		var response = restTemplate.postForEntity("/api/users", request, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).contains("Validation failed");
	}

	@Test
	void shouldListGetUpdateAndDeactivateUser() {
		var createRequest = Map.of(
			"name", "Flow Bear",
			"email", "flow.bear@bearflow.local",
			"password", "password123"
		);
		var createResponse = restTemplate.postForEntity("/api/users", createRequest, UserResponse.class);

		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		var userId = createResponse.getBody().id();

		var listResponse = restTemplate.getForEntity("/api/users", String.class);
		assertThat(listResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(listResponse.getBody()).contains("flow.bear@bearflow.local");

		var getResponse = restTemplate.getForEntity("/api/users/" + userId, UserResponse.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getResponse.getBody().name()).isEqualTo("Flow Bear");

		restTemplate.put("/api/users/" + userId, Map.of("name", "Updated Bear", "email", "updated.bear@bearflow.local"));
		var updatedResponse = restTemplate.getForEntity("/api/users/" + userId, UserResponse.class);
		assertThat(updatedResponse.getBody().name()).isEqualTo("Updated Bear");
		assertThat(updatedResponse.getBody().email()).isEqualTo("updated.bear@bearflow.local");

		restTemplate.delete("/api/users/" + userId);
		var deletedResponse = restTemplate.getForEntity("/api/users/" + userId, String.class);
		assertThat(deletedResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

}
