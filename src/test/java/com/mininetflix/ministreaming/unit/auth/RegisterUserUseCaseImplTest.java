package com.mininetflix.ministreaming.unit.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mininetflix.ministreaming.application.user.dto.RegisterUserInput;
import com.mininetflix.ministreaming.application.user.port.PasswordEncoder;
import com.mininetflix.ministreaming.application.user.port.UserRepository;
import com.mininetflix.ministreaming.application.user.usecase.RegisterUserUseCaseImpl;
import com.mininetflix.ministreaming.application.userprofile.port.UserProfileRepository;
import com.mininetflix.ministreaming.domain.user.User;
import com.mininetflix.ministreaming.domain.user.UserRole;
import com.mininetflix.ministreaming.domain.user.exception.EmailAlreadyExistsException;
import com.mininetflix.ministreaming.domain.user.exception.NameAlreadyExistsExecption;

@ExtendWith(MockitoExtension.class)
public class RegisterUserUseCaseImplTest {
        @Mock
        private UserRepository userRepository;

        @Mock
        private PasswordEncoder passwordEncoder;

        @InjectMocks
        private RegisterUserUseCaseImpl useCase;
        @Mock
        private UserProfileRepository userProfileRepository;

        @Test
        void shouldRegisterUserSuccessfully() {

                RegisterUserInput input = new RegisterUserInput(
                                "Luis",
                                "luis@email.com",
                                "123456");

                Mockito.when(userRepository.existsByEmail(input.email()))
                                .thenReturn(false);

                Mockito.when(userRepository.existsByName(input.name()))
                                .thenReturn(false);

                Mockito.when(passwordEncoder.encode("123456"))
                                .thenReturn("hashed-password");

                User persistedUser = new User(
                                UUID.randomUUID(),
                                "Luis",
                                "luis@email.com",
                                "hashed-password",
                                LocalDateTime.now(),
                                LocalDateTime.now(),
                                Set.of(UserRole.USER));

                Mockito.when(userRepository.save(Mockito.any(User.class)))
                                .thenReturn(persistedUser);

                useCase.execute(input);

                Mockito.verify(passwordEncoder)
                                .encode("123456");

                Mockito.verify(userRepository)
                                .save(Mockito.any(User.class));
        }

        @Test
        void shouldThrowExceptionWhenEmailAlreadyExists() {

                RegisterUserInput input = new RegisterUserInput(
                                "Luis",
                                "luis@email.com",
                                "123456");

                Mockito.when(userRepository.existsByEmail(input.email()))
                                .thenReturn(true);

                assertThrows(
                                EmailAlreadyExistsException.class,
                                () -> useCase.execute(input));

                Mockito.verify(userRepository, Mockito.never())
                                .save(Mockito.any());

                Mockito.verify(passwordEncoder, Mockito.never())
                                .encode(Mockito.anyString());
        }

        @Test
        void shouldThrowExceptionWhenNameAlreadyExists() {

                RegisterUserInput input = new RegisterUserInput(
                                "Luis",
                                "luis@email.com",
                                "123456");

                Mockito.when(userRepository.existsByEmail(input.email()))
                                .thenReturn(false);

                Mockito.when(userRepository.existsByName(input.name()))
                                .thenReturn(true);

                assertThrows(
                                NameAlreadyExistsExecption.class,
                                () -> useCase.execute(input));

                Mockito.verify(userRepository, Mockito.never())
                                .save(Mockito.any());
        }

        @Test
        void shouldSaveEncodedPassword() {

                RegisterUserInput input = new RegisterUserInput(
                                "Luis",
                                "luis@email.com",
                                "123456");

                Mockito.when(userRepository.existsByEmail(input.email()))
                                .thenReturn(false);

                Mockito.when(userRepository.existsByName(input.name()))
                                .thenReturn(false);

                Mockito.when(passwordEncoder.encode("123456"))
                                .thenReturn("HASHED");

                User persistedUser = new User(
                                UUID.randomUUID(),
                                "Luis",
                                "luis@email.com",
                                "HASHED",
                                LocalDateTime.now(),
                                LocalDateTime.now(),
                                Set.of(UserRole.USER));

                Mockito.when(userRepository.save(Mockito.any(User.class)))
                                .thenReturn(persistedUser);

                useCase.execute(input);

                ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

                Mockito.verify(userRepository)
                                .save(captor.capture());

                User capturedUser = captor.getValue();

                assertEquals("Luis", capturedUser.getName());
                assertEquals("luis@email.com", capturedUser.getEmail());
                assertEquals("HASHED", capturedUser.getPasswordHash());
        }
}
