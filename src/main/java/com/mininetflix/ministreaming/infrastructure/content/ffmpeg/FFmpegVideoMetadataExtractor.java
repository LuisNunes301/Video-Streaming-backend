package com.mininetflix.ministreaming.infrastructure.content.ffmpeg;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

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

            // duration + size
            ProcessBuilder formatPb = new ProcessBuilder(
                    "ffprobe",
                    "-v", "error",
                    "-show_entries", "format=duration,size",
                    "-of", "default=noprint_wrappers=1:nokey=1",
                    originalFile.getAbsolutePath());

            Process formatProcess = formatPb.start();

            BufferedReader formatReader = new BufferedReader(
                    new InputStreamReader(formatProcess.getInputStream()));

            double duration = Double.parseDouble(formatReader.readLine());
            long size = Long.parseLong(formatReader.readLine());

            formatProcess.waitFor();

            // resolution
            ProcessBuilder streamPb = new ProcessBuilder(
                    "ffprobe",
                    "-v", "error",
                    "-select_streams", "v:0",
                    "-show_entries", "stream=width,height",
                    "-of", "csv=p=0",
                    originalFile.getAbsolutePath());

            Process streamProcess = streamPb.start();

            BufferedReader streamReader = new BufferedReader(
                    new InputStreamReader(streamProcess.getInputStream()));

            String[] resolution = streamReader.readLine().split(",");

            int width = Integer.parseInt(resolution[0]);
            int height = Integer.parseInt(resolution[1]);

            streamProcess.waitFor();

            return new VideoMetadata(
                    duration,
                    size,
                    width,
                    height);

        } catch (Exception e) {
            throw new RuntimeException("Metadata extraction failed", e);
        } finally {
            originalFile.delete();
        }
    }
}