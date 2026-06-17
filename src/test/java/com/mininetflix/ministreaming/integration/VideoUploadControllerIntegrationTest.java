package com.mininetflix.ministreaming.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mininetflix.ministreaming.application.content.dto.UploadVideoInput;
import com.mininetflix.ministreaming.application.content.dto.UploadVideoOutput;
import com.mininetflix.ministreaming.application.content.port.VideoCatalogRepository;
import com.mininetflix.ministreaming.application.content.usecase.GetVideoByIdUseCase;
import com.mininetflix.ministreaming.application.content.usecase.ListVideosByCategoryUseCase;
import com.mininetflix.ministreaming.application.content.usecase.ListVideosUseCase;
import com.mininetflix.ministreaming.application.content.usecase.SearchVideosUseCase;
import com.mininetflix.ministreaming.application.content.usecase.UploadVideoUseCase;
import com.mininetflix.ministreaming.domain.content.VideoCategory;
import com.mininetflix.ministreaming.domain.content.VideoStatus;
import com.mininetflix.ministreaming.web.controller.content.VideoUploadController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(VideoUploadController.class)
@Import(WebMvcTestSecurityConfig.class)
class VideoUploadControllerIntegrationTest {
  @Autowired private MockMvc mockMvc;

  @MockitoBean private UploadVideoUseCase uploadVideoUseCase;

  @MockitoBean private GetVideoByIdUseCase getVideoByIdUseCase;

  @MockitoBean private ListVideosUseCase listVideosUseCase;

  @MockitoBean private ListVideosByCategoryUseCase listVideosByCategoryUseCase;

  @MockitoBean private SearchVideosUseCase searchVideosUseCase;

  @MockitoBean private VideoCatalogRepository catalogRepository;

  @Test
  void shouldUploadVideoSuccessfully() throws Exception {

    MockMultipartFile file =
        new MockMultipartFile("file", "video.mp4", "video/mp4", "fake-video-content".getBytes());

    UploadVideoOutput output =
        new UploadVideoOutput(
            "123", "My Video", "123/original.mp4", VideoCategory.MUSIC, VideoStatus.PROCESSING);

    when(uploadVideoUseCase.execute(any(UploadVideoInput.class))).thenReturn(output);

    mockMvc
        .perform(
            multipart("/videos/upload")
                .file(file)
                .param("title", "My Video")
                .param("category", "MUSIC"))
        .andExpect(status().isAccepted())
        .andExpect(jsonPath("$.id").value("123"))
        .andExpect(jsonPath("$.title").value("My Video"))
        .andExpect(jsonPath("$.objectKey").value("123/original.mp4"))
        .andExpect(jsonPath("$.status").value("PROCESSING"));

    verify(uploadVideoUseCase).execute(any(UploadVideoInput.class));
  }
}
