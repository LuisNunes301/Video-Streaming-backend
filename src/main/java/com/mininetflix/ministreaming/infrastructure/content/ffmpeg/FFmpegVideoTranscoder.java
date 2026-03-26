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

        File inputFile = storageService.download(objectKey);
        String basePath = videoId + "/hls/";

        File outputDir = new File("/tmp/" + basePath);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        try {

            ProcessBuilder pb = new ProcessBuilder(
                    "ffmpeg",
                    "-i", inputFile.getAbsolutePath(),

                    "-filter_complex",
                    "[0:v]split=4[v1][v2][v3][v4];" +
                            "[v1]scale=1920:1080[v1out];" +
                            "[v2]scale=1280:720[v2out];" +
                            "[v3]scale=854:480[v3out];" +
                            "[v4]scale=640:360[v4out]",

                    "-map", "[v1out]", "-map", "0:a",
                    "-map", "[v2out]", "-map", "0:a",
                    "-map", "[v3out]", "-map", "0:a",
                    "-map", "[v4out]", "-map", "0:a",

                    "-c:v", "libx264",
                    "-preset", "fast",
                    "-crf", "23",
                    "-c:a", "aac",

                    "-b:v:0", "5000k",
                    "-b:v:1", "3000k",
                    "-b:v:2", "1500k",
                    "-b:v:3", "800k",

                    "-f", "hls",
                    "-hls_time", "6",
                    "-hls_playlist_type", "vod",

                    "-hls_segment_filename",
                    outputDir.getAbsolutePath() + "/v%v/fileSequence%d.ts",

                    "-master_pl_name", "master.m3u8",

                    "-var_stream_map",
                    "v:0,a:0 v:1,a:1 v:2,a:2 v:3,a:3",

                    outputDir.getAbsolutePath() + "/v%v/prog_index.m3u8");

            pb.redirectErrorStream(true);

            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[FFMPEG] " + line);
                }
            }

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException("FFmpeg failed");
            }

            uploadDirectory(outputDir, basePath);

            return basePath + "master.m3u8";

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            deleteDirectory(outputDir);
            inputFile.delete();
        }
    }

    private void uploadDirectory(File dir, String basePath) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                uploadDirectory(file, basePath + file.getName() + "/");
            } else {
                storageService.uploadFile(basePath + file.getName(), file);
            }
        }
    }

    private void deleteDirectory(File dir) {
        if (dir == null || !dir.exists())
            return;
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                deleteDirectory(file);
            } else {
                file.delete();
            }
        }
        dir.delete();
    }
}