package com.mininetflix.ministreaming.web.controller.profile;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mininetflix.ministreaming.application.userprofile.dto.UpdateProfileOutput;
import com.mininetflix.ministreaming.application.userprofile.dto.UpdateProfileRequest;
import com.mininetflix.ministreaming.application.userprofile.usecase.GetUserProfileUseCase;
import com.mininetflix.ministreaming.application.userprofile.usecase.UpdateProfileUseCase;
import com.mininetflix.ministreaming.web.controller.profile.dto.UserProfileResponse;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final GetUserProfileUseCase getUserProfileUseCase;
    private final UpdateProfileUseCase updateProfileUseCase;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> me(
            Authentication authentication) {

        UserProfileResponse response = getUserProfileUseCase.execute(
                authentication.getName());

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<UpdateProfileOutput> update(
            Authentication authentication,
            @RequestBody UpdateProfileRequest request) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                updateProfileUseCase.execute(
                        userId,
                        request));
    }
}
