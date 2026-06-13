package com.mininetflix.ministreaming.application.profilefavorite.usecase;

import java.util.List;
import java.util.UUID;

import com.mininetflix.ministreaming.application.profilefavorite.dto.FavoriteResponse;

public interface ListFavoritesUseCase {

    List<FavoriteResponse> execute(
            UUID userId);
}