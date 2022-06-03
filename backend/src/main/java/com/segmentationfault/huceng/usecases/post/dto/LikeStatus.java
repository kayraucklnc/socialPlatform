package com.segmentationfault.huceng.usecases.post.dto;

import lombok.Data;

@Data
public class LikeStatus {
    boolean isLiked;

    public LikeStatus(boolean isLiked) {
        this.isLiked = isLiked;
    }
}
