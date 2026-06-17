package com.mininetflix.ministreaming.web.controller.profile.dto;

import java.util.UUID;

public record UserProfileResponse(UUID userId, String nickname, String avatarKey, String bio) {}
