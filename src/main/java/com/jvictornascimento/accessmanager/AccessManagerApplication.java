package com.jvictornascimento.accessmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class AccessManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccessManagerApplication.class, args);
	}

}
