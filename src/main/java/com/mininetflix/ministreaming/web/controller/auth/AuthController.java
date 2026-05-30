package com.mininetflix.ministreaming.web.controller.auth;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mininetflix.ministreaming.application.user.dto.AuthenticateUserInput;
import com.mininetflix.ministreaming.application.user.dto.AuthenticateUserOutput;
import com.mininetflix.ministreaming.application.user.dto.GetCurrentUserOutput;
import com.mininetflix.ministreaming.application.user.dto.RegisterUserInput;
import com.mininetflix.ministreaming.application.user.usecase.AuthenticateUserUseCase;
import com.mininetflix.ministreaming.application.user.usecase.GetCurrentUserUseCase;
import com.mininetflix.ministreaming.application.user.usecase.RegisterUserUseCase;

import com.mininetflix.ministreaming.web.controller.auth.dto.AuthRequest;
import com.mininetflix.ministreaming.web.controller.auth.dto.AuthResponse;
import com.mininetflix.ministreaming.web.controller.auth.dto.RegisterRequest;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {
                RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
public class AuthController {

        private final RegisterUserUseCase registerUserUseCase;
        private final AuthenticateUserUseCase authenticateUserUseCase;
        private final GetCurrentUserUseCase getCurrentUserUseCase;

        public AuthController(RegisterUserUseCase registerUserUseCase,
                        AuthenticateUserUseCase authenticateUserUseCase,
                        GetCurrentUserUseCase getCurrentUserUseCase) {
                this.registerUserUseCase = registerUserUseCase;
                this.authenticateUserUseCase = authenticateUserUseCase;
                this.getCurrentUserUseCase = getCurrentUserUseCase;
        }

        @PostMapping("/register")
        public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest request) {

                RegisterUserInput input = new RegisterUserInput(
                                request.name(),
                                request.email(),
                                request.password());

                registerUserUseCase.execute(input);

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(Map.of("message", "User registered successfully"));
        }

        @PostMapping("/login")
        public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {

                AuthenticateUserInput input = new AuthenticateUserInput(
                                request.email(),
                                request.password());

                AuthenticateUserOutput output = authenticateUserUseCase.execute(input);

                return ResponseEntity.ok(new AuthResponse(output.token()));
        }

        @GetMapping("/me")
        public ResponseEntity<GetCurrentUserOutput> me(Authentication authentication) {

                String userId = authentication.getName();

                GetCurrentUserOutput output = getCurrentUserUseCase.execute(userId);

                return ResponseEntity.ok(output);
        }
}