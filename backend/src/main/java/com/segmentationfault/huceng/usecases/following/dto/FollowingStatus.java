package com.segmentationfault.huceng.usecases.following.dto;

import lombok.Data;

@Data
public class FollowingStatus {
    boolean isFollowing;

    public FollowingStatus(boolean isFollowing) {
        this.isFollowing = isFollowing;
    }
}
