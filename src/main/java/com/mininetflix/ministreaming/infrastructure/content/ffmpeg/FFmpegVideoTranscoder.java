package com.mininetflix.ministreaming.infrastructure.content.ffmpeg;

import com.mininetflix.ministreaming.application.content.port.VideoStorageService;
import com.mininetflix.ministreaming.application.content.port.VideoTranscoder;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FFmpegVideoTranscoder implements VideoTranscoder {

  private final VideoStorageService storageService;

  private static final List<VideoProfile> PROFILES =
      List.of(
          new VideoProfile("1080p", 1920, 1080, "5000k"),
          new VideoProfile("720p", 1280, 720, "3000k"),
          new VideoProfile("480p", 854, 480, "1500k"),
          new VideoProfile("360p", 640, 360, "800k"));

  @Override
  public String transcodeToHls(String videoId, String objectKey, int inputWidth) {

    File inputFile = storageService.download(objectKey);
    String basePath = videoId + "/hls/";

    File outputDir = new File("/tmp/" + basePath);
    if (!outputDir.exists()) {
      outputDir.mkdirs();
    }

    try {

      List<VideoProfile> profiles = selectProfiles(inputWidth);

      if (profiles.isEmpty()) {
        throw new RuntimeException("No valid profiles for this video");
      }

      List<String> command = new ArrayList<>();

      command.add("ffmpeg");
      command.add("-y");
      command.add("-i");
      command.add(inputFile.getAbsolutePath());

      command.add("-threads");
      command.add(String.valueOf(Runtime.getRuntime().availableProcessors()));

      command.add("-filter_complex");
      command.add(buildFilterComplex(profiles));

      for (int i = 0; i < profiles.size(); i++) {
        command.add("-map");
        command.add("[v" + i + "out]");
        command.add("-map");
        command.add("0:a");
      }

      command.add("-c:v");
      command.add("libx264");
      command.add("-preset");
      command.add("fast");
      command.add("-crf");
      command.add("23");

      command.add("-c:a");
      command.add("aac");

      for (int i = 0; i < profiles.size(); i++) {
        command.add("-b:v:" + i);
        command.add(profiles.get(i).bitrate());
      }

      command.add("-f");
      command.add("hls");

      command.add("-hls_time");
      command.add("6");

      command.add("-hls_playlist_type");
      command.add("vod");

      command.add("-hls_segment_filename");
      command.add(outputDir.getAbsolutePath() + "/v%v/fileSequence%d.ts");

      command.add("-master_pl_name");
      command.add("master.m3u8");

      command.add("-var_stream_map");
      command.add(buildVarStreamMap(profiles.size()));

      command.add(outputDir.getAbsolutePath() + "/v%v/prog_index.m3u8");

      ProcessBuilder pb = new ProcessBuilder(command);
      pb.redirectErrorStream(true);

      Process process = pb.start();

      try (BufferedReader reader =
          new BufferedReader(new InputStreamReader(process.getInputStream()))) {

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

  private List<VideoProfile> selectProfiles(int inputWidth) {
    return PROFILES.stream().filter(p -> p.width() <= inputWidth).toList();
  }

  private String buildFilterComplex(List<VideoProfile> profiles) {

    StringBuilder filter = new StringBuilder();

    filter.append("[0:v]split=").append(profiles.size());

    for (int i = 0; i < profiles.size(); i++) {
      filter.append("[v").append(i).append("]");
    }

    filter.append(";");

    for (int i = 0; i < profiles.size(); i++) {
      var p = profiles.get(i);

      filter
          .append("[v")
          .append(i)
          .append("]")
          .append("scale=")
          .append(p.width())
          .append(":")
          .append(p.height())
          .append("[v")
          .append(i)
          .append("out];");
    }

    return filter.toString();
  }

  private String buildVarStreamMap(int size) {
    StringBuilder map = new StringBuilder();

    for (int i = 0; i < size; i++) {
      map.append("v:").append(i).append(",a:").append(i).append(" ");
    }

    return map.toString().trim();
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
    if (dir == null || !dir.exists()) return;

    for (File file : dir.listFiles()) {
      if (file.isDirectory()) {
        deleteDirectory(file);
      } else {
        file.delete();
      }
    }
    dir.delete();
  }

  private record VideoProfile(String name, int width, int height, String bitrate) {}
}
