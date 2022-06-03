package com.segmentationfault.huceng.usecases.comment.dto;

import lombok.Data;

@Data
public class CommentLikeStatus {
    boolean isLiked;

    public CommentLikeStatus(boolean isLiked) {
        this.isLiked = isLiked;
    }
}
