package com.jvictornascimento.accessmanager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {

	private List<String> allowedOrigins = List.of();

	private List<String> allowedMethods = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS");

	private List<String> allowedHeaders = List.of("Authorization", "Content-Type");

	private boolean allowCredentials = true;

	public List<String> getAllowedOrigins() {
		return allowedOrigins;
	}

	public void setAllowedOrigins(List<String> allowedOrigins) {
		this.allowedOrigins = sanitize(allowedOrigins);
	}

	public List<String> getAllowedMethods() {
		return allowedMethods;
	}

	public void setAllowedMethods(List<String> allowedMethods) {
		this.allowedMethods = sanitize(allowedMethods);
	}

	public List<String> getAllowedHeaders() {
		return allowedHeaders;
	}

	public void setAllowedHeaders(List<String> allowedHeaders) {
		this.allowedHeaders = sanitize(allowedHeaders);
	}

	public boolean isAllowCredentials() {
		return allowCredentials;
	}

	public void setAllowCredentials(boolean allowCredentials) {
		this.allowCredentials = allowCredentials;
	}

	private List<String> sanitize(List<String> values) {
		if (values == null) {
			return List.of();
		}

		return values.stream()
			.map(String::trim)
			.filter(value -> !value.isBlank())
			.toList();
	}

}
