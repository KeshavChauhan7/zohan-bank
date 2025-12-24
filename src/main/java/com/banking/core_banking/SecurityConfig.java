package com.banking.core_banking;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// connection to your database
import com.banking.core_banking.User; 
import com.banking.core_banking.UserRepository; 

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Allow simple login without tokens
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/css/**", "/js/**").permitAll() // Public access
                .anyRequest().authenticated() // Everything else requires login
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true) // Redirect to home on success
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // --- THE FIX IS HERE ---
    // Instead of "InMemoryUserDetailsManager", we actully check the database!
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            // 1. Someone tries to login with "keshav"
            // 2. We look up "keshav" in the database (findByEmployeeId)
            User user = userRepository.findByEmployeeId(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            // 3. We give the user's data to Spring Security to check the password
            return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmployeeId())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
        };
    }
}