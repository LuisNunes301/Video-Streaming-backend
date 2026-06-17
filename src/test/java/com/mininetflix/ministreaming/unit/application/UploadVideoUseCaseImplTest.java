package com.mininetflix.ministreaming.unit.application;

import com.mininetflix.ministreaming.application.content.dto.UploadVideoInput;
import com.mininetflix.ministreaming.application.content.dto.UploadVideoOutput;
import com.mininetflix.ministreaming.application.content.port.DomainEventPublisher;
import com.mininetflix.ministreaming.application.content.port.VideoCatalogRepository;
import com.mininetflix.ministreaming.application.content.port.VideoStorageService;
import com.mininetflix.ministreaming.application.content.usecase.UploadVideoUseCaseImpl;
import com.mininetflix.ministreaming.domain.content.VideoCategory;
import com.mininetflix.ministreaming.domain.content.VideoContent;
import com.mininetflix.ministreaming.domain.content.event.VideoUploadedEvent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class UploadVideoUseCaseImplTest {

  @Mock private VideoStorageService storageService;

  @Mock private VideoCatalogRepository catalogRepository;

  @Mock private DomainEventPublisher eventPublisher;

  @InjectMocks private UploadVideoUseCaseImpl useCase;

  @Test
  void shouldUploadVideoSuccessfully() {

    MockMultipartFile file =
        new MockMultipartFile("file", "video.mp4", "video/mp4", "fake-video-content".getBytes());

    UploadVideoInput input = new UploadVideoInput("My Video", VideoCategory.MUSIC, file);

    UploadVideoOutput output = useCase.execute(input);

    Assertions.assertThat(output).isNotNull();

    Assertions.assertThat(output.title()).isEqualTo("My Video");

    Assertions.assertThat(output.objectKey()).contains("original.mp4");

    Mockito.verify(storageService).upload(Mockito.anyString(), Mockito.eq(file));

    Mockito.verify(catalogRepository).save(Mockito.any(VideoContent.class));

    Mockito.verify(eventPublisher).publish(Mockito.any(VideoUploadedEvent.class));
  }
}
