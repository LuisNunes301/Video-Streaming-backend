package com.mininetflix.ministreaming.application.playback.usecase;

import java.util.List;

import com.mininetflix.ministreaming.application.playback.dto.ContinueWatchingItem;

public interface GetContinueWatchingUseCase {

    List<ContinueWatchingItem> execute(String userId);
}