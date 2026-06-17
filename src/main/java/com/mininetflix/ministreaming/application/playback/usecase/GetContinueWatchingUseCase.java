package com.mininetflix.ministreaming.application.playback.usecase;

import com.mininetflix.ministreaming.application.playback.dto.ContinueWatchingItem;
import java.util.List;

public interface GetContinueWatchingUseCase {

  List<ContinueWatchingItem> execute(String userId);
}
