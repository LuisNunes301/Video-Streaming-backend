package com.mininetflix.ministreaming.infrastructure.content.ffmpeg;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.springframework.stereotype.Component;

import com.mininetflix.ministreaming.application.content.port.VideoStorageService;
import com.mininetflix.ministreaming.application.content.port.VideoTranscoder;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FFmpegVideoTranscoder implements VideoTranscoder {

    private final VideoStorageService storageService;

    @Override
    public String transcodeToHls(String videoId, String objectKey) {

        try {
            File inputFile = storageService.download(objectKey);

            String basePath = videoId + "/hls/";

            File outputDir = new File("/tmp/" + basePath);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            String outputPlaylist = outputDir.getAbsolutePath() + "/master.m3u8";

            ProcessBuilder pb = new ProcessBuilder(
                    "ffmpeg",
                    "-i", inputFile.getAbsolutePath(),
                    "-codec:", "copy",
                    "-start_number", "0",
                    "-hls_time", "10",
                    "-hls_list_size", "0",
                    "-f", "hls",
                    outputPlaylist);

            pb.redirectErrorStream(true);

            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                while (reader.readLine() != null) {
                }
            }

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException("FFmpeg failed");
            }

            File[] files = outputDir.listFiles();

            if (files != null) {
                for (File file : files) {
                    String key = basePath + file.getName();
                    storageService.uploadFile(key, file);
                }
            }

            return basePath + "master.m3u8";

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}