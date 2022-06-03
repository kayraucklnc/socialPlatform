package com.segmentationfault.huceng.usecases.profile.controller;

import com.segmentationfault.huceng.entity.Profile;
import com.segmentationfault.huceng.usecases.profile.dto.ProfileRequest;
import com.segmentationfault.huceng.usecases.profile.dto.UpdateProfileAboutRequest;
import com.segmentationfault.huceng.usecases.profile.dto.UpdateProfileBannerRequest;
import com.segmentationfault.huceng.usecases.profile.dto.UpdateProfilePictureRequest;
import com.segmentationfault.huceng.usecases.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/profile")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping
    public Profile getProfile() {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Current user: {}", currentUser);
        return profileService.getCurrentUserProfile(currentUser);
    }

    @GetMapping("/{username}")
    public Profile getProfileById(@PathVariable String username) {
        return profileService.getProfileByUsername(username);
    }

    @PostMapping("/create")
    public void createNewProfile(@RequestBody ProfileRequest request) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        profileService.createProfile(currentUser, request);
    }

    @PutMapping("/update/about")
    public void updateExistingProfileAbout(@RequestBody UpdateProfileAboutRequest about) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        profileService.updateProfileAbout(currentUser, about.getAbout());
    }

    @PutMapping("/update/picture")
    public void updateExistingProfilePicture(@RequestBody UpdateProfilePictureRequest profilePictureRequest) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        profileService.updateProfilePicture(currentUser, profilePictureRequest.getProfilePicture());
    }

    @PutMapping("/update/banner")
    public void updateExistingProfileBanner(@RequestBody UpdateProfileBannerRequest bannerRequest) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        profileService.updateProfileBanner(currentUser, bannerRequest.getBanner());
    }

    @PutMapping("/update")
    public void updateExistingProfile(@RequestBody ProfileRequest profileRequest) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        profileService.updateProfile(currentUser, profileRequest);
    }

    @DeleteMapping()
    public void deleteCurrentUserProfile() {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        profileService.deleteProfile(currentUser);
    }
}
