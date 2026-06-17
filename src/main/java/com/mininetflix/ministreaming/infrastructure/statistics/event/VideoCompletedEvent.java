package com.mininetflix.ministreaming.infrastructure.statistics.event;

public record VideoCompletedEvent(String userId, String videoId, double watchedSeconds) {}
