package com.banque.irrigationsystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IrrigationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(IrrigationSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner run() {
		return (String[] args) -> {

		};
	}

}
