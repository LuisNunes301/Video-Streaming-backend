package com.mininetflix.ministreaming.application.content.dto;

import org.springframework.web.multipart.MultipartFile;

public record UploadVideoInput(
                String title,
                MultipartFile file) {
}