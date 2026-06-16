package com.mininetflix.ministreaming.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mininetflix.ministreaming.application.user.port.PasswordEncoder;
import com.mininetflix.ministreaming.application.user.port.UserRepository;
import com.mininetflix.ministreaming.application.userprofile.port.UserProfileRepository;
import com.mininetflix.ministreaming.domain.user.User;
import com.mininetflix.ministreaming.domain.user.UserRole;
import com.mininetflix.ministreaming.domain.userprofile.UserProfile;

@Configuration
public class AdminMockConfig {

    @Bean
    public CommandLineRunner createAdminUser(
            UserRepository userRepository,
            UserProfileRepository profileRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            String adminEmail = "admin@ministream.com";

            if (userRepository.existsByEmail(adminEmail)) {
                return;
            }

            User admin = new User(
                    "admin",
                    adminEmail,
                    passwordEncoder.encode("admin123"));

            admin.addRole(UserRole.ADMIN);

            User savedAdmin = userRepository.save(admin);

            UserProfile profile = UserProfile.create(
                    savedAdmin.getId(),
                    savedAdmin.getName());

            profileRepository.save(profile);

        };
    }
}