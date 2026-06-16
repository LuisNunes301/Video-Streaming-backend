package com.mininetflix.ministreaming.domain.statistics;

import lombok.Getter;

@Getter
public class VideoStatistics {

    private String videoId;

    private long views;

    private long completedViews;

    private double watchedSeconds;

    private VideoStatistics() {
    }

    public static VideoStatistics create(
            String videoId) {

        VideoStatistics statistics = new VideoStatistics();

        statistics.videoId = videoId;
        statistics.views = 0;
        statistics.completedViews = 0;
        statistics.watchedSeconds = 0;

        return statistics;
    }

    public static VideoStatistics restore(
            String videoId,
            long views,
            long completedViews,
            double watchedSeconds) {

        VideoStatistics statistics = new VideoStatistics();

        statistics.videoId = videoId;
        statistics.views = views;
        statistics.completedViews = completedViews;
        statistics.watchedSeconds = watchedSeconds;

        return statistics;
    }

    public void registerCompletedView(
            double watchedSeconds) {

        this.views++;
        this.completedViews++;
        this.watchedSeconds += watchedSeconds;
    }
}