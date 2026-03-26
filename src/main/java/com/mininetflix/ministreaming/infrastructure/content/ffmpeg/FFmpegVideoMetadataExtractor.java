package com.mininetflix.ministreaming.infrastructure.content.ffmpeg;

import java.io.File;

import org.springframework.stereotype.Component;

import com.mininetflix.ministreaming.application.content.dto.VideoMetadata;
import com.mininetflix.ministreaming.application.content.port.VideoMetadataExtractor;
import com.mininetflix.ministreaming.application.content.port.VideoStorageService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FFmpegVideoMetadataExtractor implements VideoMetadataExtractor {

    private final VideoStorageService storageService;

    @Override
    public VideoMetadata extract(String objectKey) {

        File originalFile = storageService.download(objectKey);

        try {

            ProcessBuilder durationBuilder = new ProcessBuilder(
                    "ffprobe",
                    "-v", "error",
                    "-show_entries", "format=duration,size",
                    "-of", "default=noprint_wrappers=1:nokey=1",
                    originalFile.getAbsolutePath());

            Process process = durationBuilder.start();

            String output = new String(process.getInputStream().readAllBytes());
            process.waitFor();

            String[] lines = output.split("\n");

            Double duration = Double.parseDouble(lines[0]);
            Long size = Long.parseLong(lines[1]);

            return new VideoMetadata(
                    duration,
                    size,
                    "unknown");

        } catch (Exception e) {
            throw new RuntimeException("Metadata extraction failed", e);
        } finally {
            originalFile.delete();
        }
    }
}