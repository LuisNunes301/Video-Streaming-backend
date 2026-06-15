package com.mininetflix.ministreaming.web.controller.home;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mininetflix.ministreaming.application.home.dto.HomeResponse;
import com.mininetflix.ministreaming.application.home.usecase.GetHomeUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final GetHomeUseCase getHomeUseCase;

    @GetMapping
    public ResponseEntity<HomeResponse> home(
            Authentication authentication) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                getHomeUseCase.execute(userId));
    }
}