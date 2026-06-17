package com.mininetflix.ministreaming.infrastructure.content.entity;

import com.mininetflix.ministreaming.domain.content.VideoCategory;
import com.mininetflix.ministreaming.domain.content.VideoStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "videos")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class VideoEntity {

  @Id private String id;

  private String title;

  private String bucket;
  private String objectKey;

  @Enumerated(EnumType.STRING)
  private VideoStatus status;

  @Enumerated(EnumType.STRING)
  private VideoCategory category;

  private Double duration;
  private Long size;
  private Integer width;
  private Integer height;

  private String thumbnailKey;
  private String hlsPlaylistKey;

  private String processingError;

  private Instant createdAt;
  private Instant processedAt;

  @PrePersist
  public void prePersist() {
    this.createdAt = Instant.now();
  }
}
