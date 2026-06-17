package com.mininetflix.ministreaming.application.profilefavorite.usecase;

import com.mininetflix.ministreaming.application.profilefavorite.dto.FavoriteResponse;
import java.util.List;
import java.util.UUID;

public interface ListFavoritesUseCase {

  List<FavoriteResponse> execute(UUID userId);
}
