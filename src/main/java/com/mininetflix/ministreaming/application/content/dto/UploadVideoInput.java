package com.mininetflix.ministreaming.application.content.dto;

import org.springframework.web.multipart.MultipartFile;

import com.mininetflix.ministreaming.domain.content.VideoCategory;

public record UploadVideoInput(
        String title,
        VideoCategory category,
        MultipartFile file) {
}