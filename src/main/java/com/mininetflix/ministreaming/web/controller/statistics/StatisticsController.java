package com.mininetflix.ministreaming.web.controller.statistics;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mininetflix.ministreaming.application.home.dto.TrendingVideoResponse;
import com.mininetflix.ministreaming.application.home.usecase.GetTrendingVideosUseCase;
import com.mininetflix.ministreaming.application.statistics.dto.VideoStatisticsResponse;
import com.mininetflix.ministreaming.application.statistics.usecase.GetVideoStatisticsUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final GetVideoStatisticsUseCase useCase;
    private final GetTrendingVideosUseCase getTrendingVideosUseCase;

    @GetMapping("/video/{videoId}")
    public ResponseEntity<VideoStatisticsResponse> getVideoStatistics(
            @PathVariable String videoId) {

        return ResponseEntity.ok(
                useCase.execute(videoId));
    }

    @GetMapping("/trending")
    public ResponseEntity<List<TrendingVideoResponse>> getTrending() {

        return ResponseEntity.ok(
                getTrendingVideosUseCase.execute());
    }
}