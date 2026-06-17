package com.mininetflix.ministreaming.domain.content.event;

import java.io.Serializable;

public record VideoUploadedEvent(String videoId, String objectKey) implements Serializable {}
