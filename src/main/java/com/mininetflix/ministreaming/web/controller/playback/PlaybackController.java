package com.mininetflix.ministreaming.web.controller.playback;

import com.mininetflix.ministreaming.application.playback.dto.ContinueWatchingItem;
import com.mininetflix.ministreaming.application.playback.dto.SavePlaybackProgressInput;
import com.mininetflix.ministreaming.application.playback.dto.StartPlaybackOutput;
import com.mininetflix.ministreaming.application.playback.usecase.GetContinueWatchingUseCase;
import com.mininetflix.ministreaming.application.playback.usecase.GetPlaybackProgressUseCase;
import com.mininetflix.ministreaming.application.playback.usecase.SavePlaybackProgressUseCase;
import com.mininetflix.ministreaming.application.playback.usecase.StartPlaybackUseCase;
import com.mininetflix.ministreaming.domain.playback.PlaybackState;
import com.mininetflix.ministreaming.web.controller.playback.dto.PlaybackProgressRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/playback")
@RequiredArgsConstructor
public class PlaybackController {

  private final StartPlaybackUseCase startPlaybackUseCase;
  private final SavePlaybackProgressUseCase savePlaybackProgressUseCase;
  private final GetPlaybackProgressUseCase getPlaybackProgressUseCase;
  private final GetContinueWatchingUseCase getContinueWatchingUseCase;

  // start playback about content id
  @GetMapping("/start/{contentId}")
  public ResponseEntity<StartPlaybackOutput> start(
      Authentication authentication, @PathVariable String contentId) {

    String userId = authentication.getName();

    return ResponseEntity.ok(startPlaybackUseCase.execute(userId, contentId));
  }

  // post progress about content id
  @PostMapping("/progress")
  public ResponseEntity<Void> progress(
      Authentication authentication, @RequestBody PlaybackProgressRequest request) {

    String userId = authentication.getName();

    SavePlaybackProgressInput input =
        new SavePlaybackProgressInput(userId, request.getContentId(), request.getCurrentTime());

    savePlaybackProgressUseCase.execute(input);

    return ResponseEntity.ok().build();
  }

  // list of video incomplete
  @GetMapping("/continue")
  public ResponseEntity<List<ContinueWatchingItem>> continueWatching(
      Authentication authentication) {

    String userId = authentication.getName();

    return ResponseEntity.ok(getContinueWatchingUseCase.execute(userId));
  }

  // return progress about content id
  @GetMapping("/{contentId}")
  public ResponseEntity<PlaybackState> getProgress(
      Authentication authentication, @PathVariable String contentId) {

    String userId = authentication.getName();

    return getPlaybackProgressUseCase
        .execute(userId, contentId)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}
