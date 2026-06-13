package com.mininetflix.ministreaming.application.profilefavorite.dto;

import com.mininetflix.ministreaming.domain.content.VideoCategory;

public record FavoriteResponse(

                String videoId,

                String title,

                String thumbnailUrl,

                VideoCategory category) {
}