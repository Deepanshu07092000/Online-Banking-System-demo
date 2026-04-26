package com.springBootProject;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.springBootProject.Entity.User;
import com.springBootProject.Repository.UserRepository;

@SpringBootApplication
public class OnlineBankingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineBankingSystemApplication.class, args);
	}
	
	/**
     * This bean runs at startup and ensures an Admin user exists in the database.
     * It uses the project's PasswordEncoder to ensure the hash is 100% compatible.
     */
    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "admin1995@gmail.com";

            if (!userRepository.existsByEmail(adminEmail)) {
                User admin = User.builder()
                        .name("Admin User")
                        .email(adminEmail)
                        // This encodes 'admin@123' using BCrypt automatically
                        .password(passwordEncoder.encode("admin@123")) 
                        .role("ROLE_ADMIN")
                        .status("ACTIVE")
                        .formNo("1000") // Required based on your entity
                        .build();

                userRepository.save(admin);
                System.out.println(">>>>> ADMIN USER CREATED: " + adminEmail + " / password: admin@123 <<<<<");
            } else {
                System.out.println(">>>>> Admin user already exists in database. <<<<<");
            }
        };
    }

}
