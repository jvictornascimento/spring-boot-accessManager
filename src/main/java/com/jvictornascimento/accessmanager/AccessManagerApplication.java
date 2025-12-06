package com.jvictornascimento.accessmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.FilterOutputStream;

@SpringBootApplication
public class AccessManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccessManagerApplication.class, args);
	}

}
