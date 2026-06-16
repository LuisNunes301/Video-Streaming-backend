package com.mininetflix.ministreaming.application.statistics.dto;

public record VideoStatisticsResponse(

        String videoId,

        long views,

        long completedViews,

        double watchedSeconds) {
}