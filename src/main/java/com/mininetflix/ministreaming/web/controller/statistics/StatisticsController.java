package com.mininetflix.ministreaming.web.controller.statistics;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mininetflix.ministreaming.application.statistics.dto.VideoStatisticsResponse;
import com.mininetflix.ministreaming.application.statistics.usecase.GetVideoStatisticsUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final GetVideoStatisticsUseCase useCase;

    @GetMapping("/video/{videoId}")
    public ResponseEntity<VideoStatisticsResponse> getVideoStatistics(
            @PathVariable String videoId) {

        return ResponseEntity.ok(
                useCase.execute(videoId));
    }
}