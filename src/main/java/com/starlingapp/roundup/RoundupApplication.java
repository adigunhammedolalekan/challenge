package com.starlingapp.roundup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.starlingapp.roundup.*"})
public class RoundupApplication {
	public static void main(String[] args) {
		SpringApplication.run(RoundupApplication.class, args);
	}
}
