package com.mininetflix.ministreaming.domain.content;

import java.time.Instant;

public class VideoContent {

    private String id;
    private String title;

    private String objectKey;

    private VideoStatus status;

    private Double duration;
    private Long size;
    private Integer width;
    private Integer height;

    private String thumbnailUrl;
    private String hlsPlaylistUrl;

    private String processingError;

    private Instant createdAt;
    private Instant processedAt;

    private VideoContent() {
    }

    public static VideoContent create(
            String id,
            String title,
            String objectKey) {

        VideoContent video = new VideoContent();
        video.id = id;
        video.title = title;
        video.objectKey = objectKey;
        video.status = VideoStatus.UPLOADING;
        video.createdAt = Instant.now();

        return video;
    }

    public static VideoContent restore(
            String id,
            String title,
            String objectKey,
            VideoStatus status,
            Double duration,
            Long size,
            Integer width,
            Integer height,
            String thumbnailUrl,
            String hlsPlaylistUrl,
            String processingError,
            Instant createdAt,
            Instant processedAt) {

        VideoContent video = new VideoContent();
        video.id = id;
        video.title = title;
        video.objectKey = objectKey;
        video.status = status;
        video.duration = duration;
        video.width = width;
        video.height = height;
        video.size = size;
        video.thumbnailUrl = thumbnailUrl;
        video.hlsPlaylistUrl = hlsPlaylistUrl;
        video.processingError = processingError;
        video.createdAt = createdAt;
        video.processedAt = processedAt;

        return video;
    }

    public void markProcessing() {
        if (this.status == VideoStatus.PROCESSING) {
            return;
        }

        if (this.status != VideoStatus.UPLOADING) {
            throw new IllegalStateException(
                    "Video can only move to PROCESSING from UPLOADING");
        }

        this.status = VideoStatus.PROCESSING;
    }

    public void markReady(
            Double duration,
            Long size,
            Integer width,
            Integer height,
            String thumbnailUrl,
            String hlsPlaylistUrl) {

        if (this.status != VideoStatus.PROCESSING) {
            throw new IllegalStateException(
                    "Video can only move to READY from PROCESSING");
        }

        this.duration = duration;
        this.size = size;
        this.width = width;
        this.height = height;
        this.thumbnailUrl = thumbnailUrl;
        this.hlsPlaylistUrl = hlsPlaylistUrl;
        this.processingError = null;

        this.status = VideoStatus.READY;
        this.processedAt = Instant.now();
    }

    public void markFailed(String error) {
        if (this.status != VideoStatus.PROCESSING) {
            throw new IllegalStateException(
                    "Video can only FAIL from PROCESSING");
        }

        this.status = VideoStatus.FAILED;
        this.processingError = error;
        this.processedAt = Instant.now();
    }

    public boolean isActive() {
        return this.status == VideoStatus.READY;
    }

    public boolean isProcessing() {
        return this.status == VideoStatus.PROCESSING;
    }

    public boolean isFailed() {
        return this.status == VideoStatus.FAILED;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public VideoStatus getStatus() {
        return status;
    }

    public Double getDuration() {
        return duration;
    }

    public Long getSize() {
        return size;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getHlsPlaylistUrl() {
        return hlsPlaylistUrl;
    }

    public String getProcessingError() {
        return processingError;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }
}