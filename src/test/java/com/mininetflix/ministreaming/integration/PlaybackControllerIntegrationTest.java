package com.mininetflix.ministreaming.integration;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.verify;

import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;

import org.springframework.http.MediaType;

import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.mininetflix.ministreaming.application.playback.dto.StartPlaybackOutput;

import com.mininetflix.ministreaming.application.playback.usecase.GetContinueWatchingUseCase;

import com.mininetflix.ministreaming.application.playback.usecase.GetPlaybackProgressUseCase;

import com.mininetflix.ministreaming.application.playback.usecase.SavePlaybackProgressUseCase;

import com.mininetflix.ministreaming.application.playback.usecase.StartPlaybackUseCase;

import com.mininetflix.ministreaming.domain.playback.PlaybackState;

import com.mininetflix.ministreaming.web.controller.playback.PlaybackController;

import com.mininetflix.ministreaming.web.controller.playback.dto.PlaybackProgressRequest;

@WebMvcTest(PlaybackController.class)
@Import(WebMvcTestSecurityConfig.class)
public class PlaybackControllerIntegrationTest {

    @Autowired

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean

    private StartPlaybackUseCase startPlaybackUseCase;

    @MockitoBean

    private SavePlaybackProgressUseCase savePlaybackProgressUseCase;

    @MockitoBean

    private GetPlaybackProgressUseCase getPlaybackProgressUseCase;

    @MockitoBean

    private GetContinueWatchingUseCase getContinueWatchingUseCase;

    @Test

    void shouldStartPlayback() throws Exception {

        StartPlaybackOutput output = new StartPlaybackOutput(

                "/videos/master.m3u8",

                0.0);

        when(startPlaybackUseCase.execute(

                "user-id",

                "video-id"))

                .thenReturn(output);

        mockMvc.perform(

                get("/playback/start/video-id")

                        .with(user("user-id")))

                .andExpect(status().isOk());

        verify(startPlaybackUseCase)

                .execute("user-id", "video-id");

    }

    @Test

    void shouldSaveProgress() throws Exception {

        PlaybackProgressRequest request = new PlaybackProgressRequest();

        request.setContentId("video-id");

        request.setCurrentTime(120.0);

        mockMvc.perform(

                post("/playback/progress")

                        .with(user("user-id"))

                        .contentType(MediaType.APPLICATION_JSON)

                        .content(

                                objectMapper.writeValueAsString(

                                        request)))

                .andExpect(status().isOk());

        verify(savePlaybackProgressUseCase)

                .execute(any());

    }

    @Test

    void shouldReturnContinueWatchingList() throws Exception {

        when(getContinueWatchingUseCase.execute("user-id"))

                .thenReturn(List.of());

        mockMvc.perform(

                get("/playback/continue")

                        .with(user("user-id")))

                .andExpect(status().isOk());

        verify(getContinueWatchingUseCase)

                .execute("user-id");

    }

    @Test

    void shouldReturnPlaybackProgress() throws Exception {

        PlaybackState state = Mockito.mock(PlaybackState.class);

        when(getPlaybackProgressUseCase.execute(

                "user-id",

                "video-id"))

                .thenReturn(Optional.of(state));

        mockMvc.perform(

                get("/playback/video-id")

                        .with(user("user-id")))

                .andExpect(status().isOk());

    }

    @Test

    void shouldReturn404WhenProgressDoesNotExist()

            throws Exception {

        when(getPlaybackProgressUseCase.execute(

                "user-id",

                "video-id"))

                .thenReturn(Optional.empty());

        mockMvc.perform(

                get("/playback/video-id")

                        .with(user("user-id")))

                .andExpect(status().isNotFound());

    }

}
