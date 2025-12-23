package com.banking.core_banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CoreBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreBankingApplication.class, args);
		System.out.println("✅ BANKING PORTAL IS LIVE: http://localhost:8080");
	}
}