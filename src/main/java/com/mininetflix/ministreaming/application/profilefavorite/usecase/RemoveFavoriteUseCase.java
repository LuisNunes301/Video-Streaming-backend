package com.mininetflix.ministreaming.application.profilefavorite.usecase;

import java.util.UUID;

public interface RemoveFavoriteUseCase {

    void execute(
            UUID userId,
            String videoId);
}