package com.mininetflix.ministreaming.application.playback.dto;

public record SavePlaybackProgressInput(String userId, String contentId, double currentTime) {}
