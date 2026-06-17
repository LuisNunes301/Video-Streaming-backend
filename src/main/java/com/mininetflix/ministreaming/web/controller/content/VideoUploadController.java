package com.mininetflix.ministreaming.web.controller.content;

import com.mininetflix.ministreaming.application.content.usecase.SearchVideosUseCaseImpl;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mininetflix.ministreaming.application.content.dto.UploadVideoInput;
import com.mininetflix.ministreaming.application.content.dto.UploadVideoOutput;
import com.mininetflix.ministreaming.application.content.dto.VideoResponse;
import com.mininetflix.ministreaming.application.content.port.VideoCatalogRepository;
import com.mininetflix.ministreaming.application.content.usecase.GetVideoByIdUseCase;
import com.mininetflix.ministreaming.application.content.usecase.ListVideosByCategoryUseCase;
import com.mininetflix.ministreaming.application.content.usecase.ListVideosUseCase;
import com.mininetflix.ministreaming.application.content.usecase.SearchVideosUseCase;
import com.mininetflix.ministreaming.application.content.usecase.UploadVideoUseCase;
import com.mininetflix.ministreaming.domain.content.VideoCategory;
import com.mininetflix.ministreaming.domain.content.VideoContent;

@RestController
@RequestMapping("/videos")
public class VideoUploadController {

        private final UploadVideoUseCase uploadVideoUseCase;
        private final GetVideoByIdUseCase getVideoByIdUseCase;
        private final ListVideosUseCase listVideoUseCase;
        private final ListVideosByCategoryUseCase listVideosByCategoryUseCase;
        private final SearchVideosUseCase searchVideosUseCase;

        public VideoUploadController(
                        UploadVideoUseCase uploadVideoUseCase,
                        GetVideoByIdUseCase getVideoByIdUseCase,
                        ListVideosUseCase listVideoUseCase, ListVideosByCategoryUseCase listVideosByCategoryUseCase,
                        SearchVideosUseCase searchVideosUseCase, SearchVideosUseCaseImpl searchVideosUseCaseImpl) {

                this.uploadVideoUseCase = uploadVideoUseCase;
                this.getVideoByIdUseCase = getVideoByIdUseCase;
                this.listVideoUseCase = listVideoUseCase;
                this.listVideosByCategoryUseCase = listVideosByCategoryUseCase;
                this.searchVideosUseCase = searchVideosUseCase;
        }

        @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<UploadVideoOutput> upload(
                        @RequestParam String title,
                        @RequestParam VideoCategory category,
                        @RequestParam MultipartFile file) {

                UploadVideoInput input = new UploadVideoInput(
                                title,
                                category,
                                file);

                return ResponseEntity.accepted()
                                .body(uploadVideoUseCase.execute(input));
        }

        // Listar catálogo
        @GetMapping
        public ResponseEntity<List<VideoResponse>> list() {

                return ResponseEntity.ok(
                                listVideoUseCase.execute());
        }

        @GetMapping("/{id}")
        public ResponseEntity<VideoResponse> getById(
                        @PathVariable String id) {

                return ResponseEntity.ok(
                                getVideoByIdUseCase.execute(id));
        }

        @GetMapping("/category/{category}")
        public ResponseEntity<List<VideoResponse>> byCategory(
                        @PathVariable VideoCategory category) {

                return ResponseEntity.ok(
                                listVideosByCategoryUseCase.execute(category));
        }

        @GetMapping("/search")
        public ResponseEntity<List<VideoResponse>> search(
                        @RequestParam String q) {

                return ResponseEntity.ok(
                                searchVideosUseCase.execute(q));
        }

}
