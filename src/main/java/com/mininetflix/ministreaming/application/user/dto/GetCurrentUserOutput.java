package com.mininetflix.ministreaming.application.user.dto;

import java.util.UUID;

public record GetCurrentUserOutput(
        UUID id,
        String name,
        String email) {
}