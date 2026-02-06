package com.costadelsur.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CostadelsurApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(CostadelsurApiApplication.class, args);
	}

}
