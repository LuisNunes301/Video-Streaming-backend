package com.mininetflix.ministreaming.application.content.dto;

import com.mininetflix.ministreaming.domain.content.VideoCategory;
import org.springframework.web.multipart.MultipartFile;

public record UploadVideoInput(String title, VideoCategory category, MultipartFile file) {}
