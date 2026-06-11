package com.mininetflix.ministreaming.infrastructure.content.ffmpeg;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.mininetflix.ministreaming.application.content.dto.PreviewResult;
import com.mininetflix.ministreaming.application.content.port.VideoPreviewGenerator;
import com.mininetflix.ministreaming.application.content.port.VideoStorageService;
import com.mininetflix.ministreaming.application.storage.StorageBucketEnum;
import com.mininetflix.ministreaming.application.storage.StorageService;
import com.mininetflix.ministreaming.infrastructure.storage.ThumbnailStorageService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FFmpegPreviewGenerator implements VideoPreviewGenerator {

    private final VideoStorageService videoStorageService;
    private final ThumbnailStorageService thumbnailStorageService;

    @Override
    public PreviewResult generate(String videoId, String objectKey) {

        File video = videoStorageService.download(objectKey);

        File tempDir = new File(System.getProperty("java.io.tmpdir"), "preview-" + videoId);
        tempDir.mkdirs();

        try {

            File thumbnail = new File(tempDir, "thumbnail.jpg");

            runCommand(List.of(
                    "ffmpeg",
                    "-i", video.getAbsolutePath(),
                    "-ss", "00:00:02",
                    "-vframes", "1",
                    "-q:v", "2",
                    thumbnail.getAbsolutePath()));

            File framesDir = new File(tempDir, "frames");
            framesDir.mkdirs();

            runCommand(List.of(
                    "ffmpeg",
                    "-i", video.getAbsolutePath(),
                    "-vf", "fps=1/10,scale=160:-1",
                    new File(framesDir, "frame_%03d.jpg").getAbsolutePath()));

            File[] frames = framesDir.listFiles();
            if (frames == null || frames.length == 0) {
                throw new RuntimeException("No frames generated");
            }

            File sprite = new File(tempDir, "sprite.jpg");

            int cols = 5;
            int rows = (int) Math.ceil(frames.length / (double) cols);

            runCommand(List.of(
                    "ffmpeg",
                    "-i", framesDir.getAbsolutePath() + "/frame_%03d.jpg",
                    "-filter_complex",
                    "tile=" + cols + "x" + rows,
                    sprite.getAbsolutePath()));

            File vtt = new File(tempDir, "preview.vtt");
            generateVtt(vtt, frames.length, cols);

            String basePath = "previews/" + videoId;

            String thumbnailKey = basePath + "/thumbnail.jpg";
            String spriteKey = basePath + "/sprite.jpg";
            String vttKey = basePath + "/preview.vtt";

            thumbnailStorageService.upload(
                    thumbnailKey,
                    thumbnail);

            thumbnailStorageService.upload(
                    spriteKey,
                    sprite);

            thumbnailStorageService.upload(
                    vttKey,
                    vtt);

            return new PreviewResult(thumbnailKey, spriteKey, vttKey);

        } catch (Exception e) {
            throw new RuntimeException("Preview generation failed", e);
        } finally {
            video.delete();
            deleteDirectory(tempDir);
        }
    }

    private void runCommand(List<String> command) throws IOException, InterruptedException {

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);

        Process process = pb.start();

        // consumir stdout (evita deadlock)
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {

            while (reader.readLine() != null) {
                // opcional log
            }
        }

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("FFmpeg failed: " + command);
        }
    }

    private void generateVtt(File vttFile, int totalFrames, int cols) throws IOException {

        List<String> lines = new ArrayList<>();
        lines.add("WEBVTT\n");

        int interval = 10; // segundos por frame

        for (int i = 0; i < totalFrames; i++) {

            int start = i * interval;
            int end = start + interval;

            String startTime = formatTime(start);
            String endTime = formatTime(end);

            int x = (i % cols) * 160;
            int y = (i / cols) * 90;

            lines.add(startTime + " --> " + endTime);
            lines.add("sprite.jpg#xywh=" + x + "," + y + ",160,90");
            lines.add("");
        }

        Files.write(vttFile.toPath(), lines);
    }

    private String formatTime(int seconds) {
        int h = seconds / 3600;
        int m = (seconds % 3600) / 60;
        int s = seconds % 60;

        return String.format("%02d:%02d:%02d.000", h, m, s);
    }

    private void deleteDirectory(File dir) {
        if (dir == null || !dir.exists())
            return;

        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                deleteDirectory(f);
            }
        }
        dir.delete();
    }
}