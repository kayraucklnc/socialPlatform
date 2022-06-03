package com.segmentationfault.huceng.usecases.rate.controller;

import com.segmentationfault.huceng.entity.AppUser;
import com.segmentationfault.huceng.entity.Rating;
import com.segmentationfault.huceng.entity.repository.AppUserRepository;
import com.segmentationfault.huceng.entity.repository.ProfileRepository;
import com.segmentationfault.huceng.startup.StartupService;
import com.segmentationfault.huceng.usecases.profile.service.ProfileService;
import com.segmentationfault.huceng.usecases.rate.dto.RatingRequestByProfileId;
import com.segmentationfault.huceng.usecases.rate.dto.RatingRequestByUsername;
import com.segmentationfault.huceng.usecases.rate.dto.RatingResponse;
import com.segmentationfault.huceng.usecases.rate.service.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@RequestMapping(path = "/api/rating")
@RequiredArgsConstructor
@Slf4j
public class RatingController {
    private final RatingService ratingService;
    private final StartupService startupService;
    private final ProfileService profileService;

    @PutMapping("/id")
    public RatingResponse rateProfileByProfileId(@RequestBody RatingRequestByProfileId request) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

        log.info("Current user {}", currentUser);

        AppUser appUser = startupService.getUser(currentUser);
        Double updatedRating = ratingService
                .rateProfileByProfileId(appUser.getProfile().getId(), request.getProfileId(), request.getRating());
        return new RatingResponse(updatedRating);
    }

    @PutMapping("/username")
    public RatingResponse rateProfileByUsername(@RequestBody RatingRequestByUsername request) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

        log.info("Current user {}", currentUser);

        Double updatedRating = ratingService
                .rateProfileByUsername(currentUser, request.getUsername(), request.getRating());
        return new RatingResponse(updatedRating);
    }

    @GetMapping("/username/{username}")
    public Double userRatedProfileAsByUsername(@PathVariable String username) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

        return ratingService.getRatingOfUserGivenByUser(currentUser, username);
    }

    @GetMapping("/id/{id}")
    public Double userRatedProfileAsById(@PathVariable Long id) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

        String rateeUser = startupService.getUser(id).getUsername();

        return ratingService.getRatingOfUserGivenByUser(currentUser, rateeUser);
    }

    @GetMapping({"/average/username/{username}"})
    public Double getProfileRating(@PathVariable String username) {
        return ratingService.getAverageRating(username);
    }

    @GetMapping({"/average/id/{id}"})
    public Double getProfileRating(@PathVariable Long id) {
        return ratingService.getAverageRating(id);
    }

    @GetMapping({"/raters"})
    public Collection<Rating> getRatersOfProfile() {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

        return profileService.getCurrentUserProfile(currentUser).getProfileRatings();
    }

    @GetMapping({"/ratees"})
    public Collection<Rating> getRateesOfProfile() {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

        return profileService.getCurrentUserProfile(currentUser).getRatedProfiles();
    }
}
