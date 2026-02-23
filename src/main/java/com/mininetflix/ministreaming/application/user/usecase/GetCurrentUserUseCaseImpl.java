package com.mininetflix.ministreaming.application.user.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mininetflix.ministreaming.application.user.dto.GetCurrentUserOutput;
import com.mininetflix.ministreaming.application.user.port.UserRepository;
import com.mininetflix.ministreaming.domain.user.User;

@Service
public class GetCurrentUserUseCaseImpl implements GetCurrentUserUseCase {

    private final UserRepository userRepository;

    public GetCurrentUserUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public GetCurrentUserOutput execute(String userId) {

        UUID id = UUID.fromString(userId);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new GetCurrentUserOutput(
                user.getId(),
                user.getName(),
                user.getEmail());
    }
}
