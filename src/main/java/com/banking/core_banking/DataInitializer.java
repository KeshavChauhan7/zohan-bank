package com.banking.core_banking;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // --- User 1: Keshav ---
            if (userRepository.findByEmployeeId("keshav").isEmpty()) {
                User user1 = new User();
                user1.setEmployeeId("keshav");
                user1.setName("Keshav");
                user1.setPassword(passwordEncoder.encode("horseradish"));
                user1.setRole("ADMIN");
                
                userRepository.save(user1);
                System.out.println("✅ CREATED USER: keshav");
            }

            // --- User 2: Chauhan ---
            if (userRepository.findByEmployeeId("chauhan").isEmpty()) {
                User user2 = new User();
                user2.setEmployeeId("chauhan");
                user2.setName("Chauhan");
                user2.setPassword(passwordEncoder.encode("wasabi"));
                user2.setRole("MANAGER");

                userRepository.save(user2);
                System.out.println("✅ CREATED USER: chauhan");
            }
        };
    }
}