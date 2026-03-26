package com.mininetflix.ministreaming.application.content.port;

public interface VideoTranscoder {

    String transcodeToHls(String videoId, String objectKey, int inputWidth);
}