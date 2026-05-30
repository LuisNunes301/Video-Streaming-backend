package com.mininetflix.ministreaming.integration;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.verify;

import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;

import org.springframework.http.MediaType;

import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.mininetflix.ministreaming.application.user.dto.AuthenticateUserOutput;

import com.mininetflix.ministreaming.application.user.dto.GetCurrentUserOutput;

import com.mininetflix.ministreaming.application.user.dto.RegisterUserInput;

import com.mininetflix.ministreaming.application.user.usecase.AuthenticateUserUseCase;

import com.mininetflix.ministreaming.application.user.usecase.GetCurrentUserUseCase;

import com.mininetflix.ministreaming.application.user.usecase.RegisterUserUseCase;

import com.mininetflix.ministreaming.web.controller.auth.AuthController;

import com.mininetflix.ministreaming.web.controller.auth.dto.AuthRequest;

import com.mininetflix.ministreaming.web.controller.auth.dto.RegisterRequest;

@WebMvcTest(AuthController.class)
@Import(WebMvcTestSecurityConfig.class)
public class AuthControllerIntegrationTest {

        @Autowired

        private MockMvc mockMvc;

        private final ObjectMapper objectMapper = new ObjectMapper();

        @MockitoBean

        private RegisterUserUseCase registerUserUseCase;

        @MockitoBean

        private AuthenticateUserUseCase authenticateUserUseCase;

        @MockitoBean

        private GetCurrentUserUseCase getCurrentUserUseCase;

        @Test

        void shouldRegisterUserSuccessfully() throws Exception {

                RegisterRequest request = new RegisterRequest(

                                "Luis",

                                "luis@email.com",

                                "123456");

                mockMvc.perform(

                                post("/auth/register")

                                                .contentType(MediaType.APPLICATION_JSON)

                                                .content(objectMapper.writeValueAsString(request)))

                                .andExpect(status().isCreated())

                                .andExpect(jsonPath("$.message")

                                                .value("User registered successfully"));

                verify(registerUserUseCase)

                                .execute(any(RegisterUserInput.class));

        }

        @Test

        void shouldLoginSuccessfully() throws Exception {

                AuthRequest request = new AuthRequest(

                                "luis@email.com",

                                "123456");

                when(authenticateUserUseCase.execute(any()))

                                .thenReturn(

                                                new AuthenticateUserOutput(

                                                                "jwt-token"));

                mockMvc.perform(

                                post("/auth/login")

                                                .contentType(MediaType.APPLICATION_JSON)

                                                .content(objectMapper.writeValueAsString(request)))

                                .andExpect(status().isOk())

                                .andExpect(jsonPath("$.token")

                                                .value("jwt-token"));

                verify(authenticateUserUseCase)

                                .execute(any());

        }

        @Test

        void shouldReturnCurrentUser() throws Exception {

                GetCurrentUserOutput output = new GetCurrentUserOutput(

                                UUID.randomUUID(),

                                "Luis",

                                "luis@email.com");

                when(getCurrentUserUseCase.execute("user-id"))

                                .thenReturn(output);

                mockMvc.perform(

                                get("/auth/me")

                                                .with(user("user-id")))

                                .andExpect(status().isOk())

                                .andExpect(jsonPath("$.name")

                                                .value("Luis"))

                                .andExpect(jsonPath("$.email")

                                                .value("luis@email.com"));

        }

}
