package com.example.workforce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.workforce.config.JwtConfig;

@SpringBootApplication
@EnableConfigurationProperties(JwtConfig.class)
public class WorkforceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkforceApplication.class, args);
	}

}
