package com.segmentationfault.huceng.usecases.following.service;

import com.segmentationfault.huceng.entity.AppUser;

public interface FollowingService {
    // Get user info from authentication.
    boolean checkUserFollow(AppUser targetUser);
    void followUser(AppUser user);
    void unfollowUser(AppUser user);

    boolean checkUserFollow(AppUser user, AppUser targetUser);
    void followUser(AppUser domainUser, AppUser targetUser);
    void unfollowUser(AppUser domainUser, AppUser targetUser);
}
