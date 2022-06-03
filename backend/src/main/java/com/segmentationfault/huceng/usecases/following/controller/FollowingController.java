package com.segmentationfault.huceng.usecases.following.controller;

import com.segmentationfault.huceng.entity.AppUser;
import com.segmentationfault.huceng.startup.StartupService;
import com.segmentationfault.huceng.usecases.following.dto.FollowingStatus;
import com.segmentationfault.huceng.usecases.following.service.FollowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = "/api/follow") //localhost::api/follow
@RequiredArgsConstructor
public class FollowingController {
    private final FollowingService followingService;
    private final StartupService startupService;

    @GetMapping("/{userID}")
    public FollowingStatus getFollowingStatus(@PathVariable Long userID){
        return new FollowingStatus(followingService.checkUserFollow(startupService.getUser(userID)));
    }

    @GetMapping("/username/{username}")
    public FollowingStatus getFollowingStatus(@PathVariable String username){
        return new FollowingStatus(followingService.checkUserFollow(startupService.getUser(username)));
    }

    @GetMapping("/followers")
    public Collection<AppUser> getFollowers(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = startupService.getUser(auth.getName());

        return appUser.getFollowers();
    }

    @GetMapping("/followers/{userID}")
    public Collection<AppUser> getFollowersOfUser(@PathVariable Long userID){
        AppUser appUser = startupService.getUser(userID);
        return appUser.getFollowers();
    }

    @GetMapping("/followers/username/{username}")
    public Collection<AppUser> getFollowersOfUser(@PathVariable String username){
        AppUser appUser = startupService.getUser(username);
        return appUser.getFollowers();
    }

    @GetMapping("/followedUsers")
    public Collection<AppUser> getFollowedUsers(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = startupService.getUser(auth.getName());

        return appUser.getFollowedUsers();
    }

    @PostMapping("/{userID}")
    public FollowingStatus toggleFollow(@PathVariable Long userID) {
        AppUser targetUser = startupService.getUser(userID);
        return toggleFollowAndGetStatus(targetUser);
    }

    @PostMapping("/username/{username}")
    public FollowingStatus toggleFollow(@PathVariable String username) {
        AppUser targetUser = startupService.getUser(username);
        return toggleFollowAndGetStatus(targetUser);
    }

    private FollowingStatus toggleFollowAndGetStatus(AppUser targetUser) {
        if (followingService.checkUserFollow(targetUser)) {
            followingService.unfollowUser(targetUser);
        } else {
            followingService.followUser(targetUser);
        }

        return new FollowingStatus(followingService.checkUserFollow(targetUser));
    }

}
