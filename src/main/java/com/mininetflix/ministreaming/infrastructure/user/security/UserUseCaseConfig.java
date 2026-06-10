package com.mininetflix.ministreaming.infrastructure.user.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mininetflix.ministreaming.application.user.port.PasswordEncoder;
import com.mininetflix.ministreaming.application.user.port.TokenService;
import com.mininetflix.ministreaming.application.user.port.UserRepository;
import com.mininetflix.ministreaming.application.user.usecase.AuthenticateUserUseCase;
import com.mininetflix.ministreaming.application.user.usecase.AuthenticateUserUseCaseImpl;
import com.mininetflix.ministreaming.application.user.usecase.RegisterUserUseCase;
import com.mininetflix.ministreaming.application.user.usecase.RegisterUserUseCaseImpl;
import com.mininetflix.ministreaming.application.userprofile.port.UserProfileRepository;

@Configuration
public class UserUseCaseConfig {

    @Bean
    public AuthenticateUserUseCase authenticateUserUseCase(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            TokenService tokenService) {
        return new AuthenticateUserUseCaseImpl(
                userRepository,
                passwordEncoder,
                tokenService);
    }

    @Bean
    public RegisterUserUseCase registerUserUseCase(
            UserRepository userRepository,
            UserProfileRepository userProfileRepository,
            PasswordEncoder passwordEncoder) {

        return new RegisterUserUseCaseImpl(
                userRepository,
                userProfileRepository,
                passwordEncoder);
    }
}