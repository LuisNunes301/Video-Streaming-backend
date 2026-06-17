package com.mininetflix.ministreaming.infrastructure.statistics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "video_statistics")
@Getter
@Setter
@NoArgsConstructor
public class VideoStatisticsEntity {

  @Id
  @Column(name = "video_id")
  private String videoId;

  @Column(nullable = false)
  private Long views;

  @Column(nullable = false)
  private Long completedViews;

  @Column(nullable = false)
  private Double watchedSeconds;
}
