package com.mininetflix.ministreaming.infrastructure.content.ffmpeg;

import java.io.File;
import java.io.IOException;

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

        File file = storageService.download(objectKey);

        try {

            // 🎥 duração
            ProcessBuilder durationBuilder = new ProcessBuilder(
                    "ffprobe",
                    "-v", "error",
                    "-show_entries", "format=duration",
                    "-of", "default=noprint_wrappers=1:nokey=1",
                    file.getAbsolutePath());

            Process durationProcess = durationBuilder.start();
            String durationOutput = new String(durationProcess.getInputStream().readAllBytes());
            Double duration = Double.parseDouble(durationOutput.trim());

            // 📐 resolução
            ProcessBuilder resolutionBuilder = new ProcessBuilder(
                    "ffprobe",
                    "-v", "error",
                    "-select_streams", "v:0",
                    "-show_entries", "stream=width,height",
                    "-of", "csv=s=x:p=0",
                    file.getAbsolutePath());

            Process resolutionProcess = resolutionBuilder.start();
            String resolution = new String(resolutionProcess.getInputStream().readAllBytes()).trim();

            // 📦 tamanho
            Long size = file.length();

            file.delete();

            return new VideoMetadata(
                    duration,
                    size,
                    resolution,
                    null // HLS será gerado em outro passo
            );

        } catch (IOException e) {
            throw new RuntimeException("Error extracting video metadata", e);
        }
    }
}