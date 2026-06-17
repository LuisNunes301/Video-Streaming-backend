package com.mininetflix.ministreaming.infrastructure.content.ffmpeg;

import com.mininetflix.ministreaming.application.content.dto.VideoMetadata;
import com.mininetflix.ministreaming.application.content.port.VideoMetadataExtractor;
import com.mininetflix.ministreaming.application.content.port.VideoStorageService;
import java.io.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FFmpegVideoMetadataExtractor implements VideoMetadataExtractor {

  private final VideoStorageService storageService;

  @Override
  public VideoMetadata extract(String objectKey) {

    File file = storageService.download(objectKey);

    try {

      ProcessBuilder pb =
          new ProcessBuilder(
              "ffprobe",
              "-v",
              "error",
              "-select_streams",
              "v:0",
              "-show_entries",
              "stream=width,height:format=duration,size",
              "-of",
              "default=noprint_wrappers=1:nokey=1",
              file.getAbsolutePath());

      Process process = pb.start();

      String output = new String(process.getInputStream().readAllBytes());
      process.waitFor();

      String[] lines = output.split("\n");

      // ordem agora é:
      // width
      // height
      // duration
      // size

      int width = Integer.parseInt(lines[0]);
      int height = Integer.parseInt(lines[1]);
      double duration = Double.parseDouble(lines[2]);
      long size = Long.parseLong(lines[3]);

      return new VideoMetadata(duration, size, width, height);

    } catch (Exception e) {
      throw new RuntimeException("Metadata extraction failed", e);
    } finally {
      file.delete();
    }
  }
}
