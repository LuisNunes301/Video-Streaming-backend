package com.mininetflix.ministreaming.unit.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mininetflix.ministreaming.application.user.dto.AuthenticateUserInput;
import com.mininetflix.ministreaming.application.user.dto.AuthenticateUserOutput;
import com.mininetflix.ministreaming.application.user.port.PasswordEncoder;
import com.mininetflix.ministreaming.application.user.port.TokenService;
import com.mininetflix.ministreaming.application.user.port.UserRepository;
import com.mininetflix.ministreaming.application.user.usecase.AuthenticateUserUseCaseImpl;
import com.mininetflix.ministreaming.domain.user.User;
import com.mininetflix.ministreaming.domain.user.UserRole;
import com.mininetflix.ministreaming.domain.user.exception.InvalidCredentialsException;

@ExtendWith(MockitoExtension.class)
class AuthenticateUserUseCaseImplTest {

        @Mock
        private UserRepository userRepository;

        @Mock
        private PasswordEncoder passwordEncoder;

        @Mock
        private TokenService tokenService;

        @InjectMocks
        private AuthenticateUserUseCaseImpl useCase;

        @Test
        void shouldAuthenticateSuccessfully() {

                User user = new User(
                                UUID.randomUUID(),
                                "Luis",
                                "luis@email.com",
                                "hashed-password",
                                LocalDateTime.now(),
                                LocalDateTime.now(),
                                Set.of(UserRole.USER));

                AuthenticateUserInput input = new AuthenticateUserInput(
                                "luis@email.com",
                                "123456");

                when(userRepository.findByEmail(input.email()))
                                .thenReturn(Optional.of(user));

                when(passwordEncoder.matches(
                                "123456",
                                "hashed-password"))
                                .thenReturn(true);

                when(tokenService.generateToken(
                                anyString(), any()))
                                .thenReturn("jwt-token");

                AuthenticateUserOutput output = useCase.execute(input);

                assertEquals(
                                "jwt-token",
                                output.token());
        }

        @Test
        void shouldThrowExceptionWhenUserDoesNotExist() {

                AuthenticateUserInput input = new AuthenticateUserInput(
                                "unknown@email.com",
                                "123456");

                Mockito.when(userRepository.findByEmail(input.email()))
                                .thenReturn(Optional.empty());

                assertThrows(
                                InvalidCredentialsException.class,
                                () -> useCase.execute(input));

                Mockito.verifyNoInteractions(tokenService);
        }

        @Test
        void shouldThrowExceptionWhenPasswordIsInvalid() {

                User user = new User(
                                "Luis",
                                "luis@email.com",
                                "hashed-password");

                AuthenticateUserInput input = new AuthenticateUserInput(
                                "luis@email.com",
                                "wrong-password");

                Mockito.when(userRepository.findByEmail(input.email()))
                                .thenReturn(Optional.of(user));

                Mockito.when(
                                passwordEncoder.matches(
                                                "wrong-password",
                                                "hashed-password"))
                                .thenReturn(false);

                assertThrows(
                                InvalidCredentialsException.class,
                                () -> useCase.execute(input));

                Mockito.verifyNoInteractions(tokenService);
        }
}
