package com.jvictornascimento.accessmanager.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI accessManagerOpenApi() {
		return new OpenAPI()
			.info(new Info()
				.title("Bear Control Access Weather API")
				.version("v1")
				.description("""
					API de autenticação, cadastro de usuários e consulta de clima da Bear Flow.
					O login retorna um access token JWT no corpo da resposta e envia o refresh token em cookie HTTP-only.
					""")
				.contact(new Contact()
					.name("Bear Flow")
					.email("contato@bearflow.local"))
				.license(new License()
					.name("Internal use")))
			.servers(List.of(
				new Server().url("http://localhost:8080").description("Local development"),
				new Server().url("https://api.bearflow.local").description("Production example")
			))
			.components(new Components()
				.addSecuritySchemes("bearerAuth", new SecurityScheme()
					.type(SecurityScheme.Type.HTTP)
					.scheme("bearer")
					.bearerFormat("JWT")
					.description("Informe o access token JWT retornado pelo endpoint de login.")));
	}

}
