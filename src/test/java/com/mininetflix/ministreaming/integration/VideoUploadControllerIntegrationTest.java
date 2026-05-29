package com.mininetflix.ministreaming.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.mininetflix.ministreaming.application.content.dto.UploadVideoInput;
import com.mininetflix.ministreaming.application.content.dto.UploadVideoOutput;
import com.mininetflix.ministreaming.application.content.port.VideoCatalogRepository;
import com.mininetflix.ministreaming.application.content.usecase.UploadVideoUseCase;

import com.mininetflix.ministreaming.domain.content.VideoStatus;

import com.mininetflix.ministreaming.web.controller.content.VideoUploadController;

@WebMvcTest(VideoUploadController.class)
@TestPropertySource(properties = {
        "APP_NAME=ministreaming-test"
})
class VideoUploadControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UploadVideoUseCase uploadVideoUseCase;

    @MockitoBean
    private VideoCatalogRepository catalogRepository;

    @Test
    void shouldUploadVideoSuccessfully() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "video.mp4",
                "video/mp4",
                "fake-video-content".getBytes());

        UploadVideoOutput output = new UploadVideoOutput(
                "123",
                "My Video",
                "123/original.mp4",
                VideoStatus.PROCESSING);

        when(uploadVideoUseCase.execute(any(UploadVideoInput.class)))
                .thenReturn(output);

        mockMvc.perform(
                multipart("/videos/upload")
                        .file(file)
                        .param("title", "My Video"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.title").value("My Video"))
                .andExpect(jsonPath("$.objectKey")
                        .value("123/original.mp4"))
                .andExpect(jsonPath("$.status")
                        .value("PROCESSING"));

        verify(uploadVideoUseCase)
                .execute(any(UploadVideoInput.class));
    }
}