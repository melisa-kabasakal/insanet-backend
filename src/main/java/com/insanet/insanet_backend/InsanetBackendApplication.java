package com.insanet.insanet_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.module.Configuration;

@SpringBootApplication
public class InsanetBackendApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(InsanetBackendApplication.class, args);

	}

}
