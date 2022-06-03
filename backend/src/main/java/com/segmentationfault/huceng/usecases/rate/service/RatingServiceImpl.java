package com.segmentationfault.huceng.usecases.rate.service;

import com.segmentationfault.huceng.entity.AppUser;
import com.segmentationfault.huceng.entity.Profile;
import com.segmentationfault.huceng.entity.ProfileRatingKey;
import com.segmentationfault.huceng.entity.Rating;
import com.segmentationfault.huceng.entity.repository.AppUserRepository;
import com.segmentationfault.huceng.entity.repository.ProfileRepository;
import com.segmentationfault.huceng.entity.repository.RatingRepository;
import com.segmentationfault.huceng.exception.ProfileDoesntExist;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RatingServiceImpl implements RatingService {
    private final ProfileRepository profileRepository;
    private final RatingRepository ratingRepository;


    @Override
    public Double rateProfileByProfileId(Long raterId, Long rateeId, Double ratingValue) {
        log.info("Rating by profile id.");
        Profile rater = profileRepository.findById(raterId).orElseThrow(() -> new ProfileDoesntExist(raterId));
        Profile ratee = profileRepository.findById(rateeId).orElseThrow(() -> new ProfileDoesntExist(rateeId));

        completeRating(rater, ratee, ratingValue);

        calculateRating(ratee);
        return ratee.getRating();
    }

    @Override
    public Double rateProfileByUsername(String raterUsername, String rateeUsername, Double ratingValue) {
        log.info("Rating by username.");
        Profile rater = profileRepository.findProfileByUsername(raterUsername)
                .orElseThrow(() -> new ProfileDoesntExist(raterUsername));

        Profile ratee = profileRepository.findProfileByUsername(rateeUsername)
                .orElseThrow(() -> new ProfileDoesntExist(rateeUsername));

        completeRating(rater, ratee, ratingValue);

        calculateRating(ratee);
        return ratee.getRating();
    }

    @Override
    public Double getRatingOfUserGivenByUser(String rater, String ratee) {
        Profile raterProfile = profileRepository.findProfileByUsername(rater).orElseThrow();

        Rating rating = raterProfile.getRatedProfiles().stream().filter(new Predicate<Rating>() {
            @Override
            public boolean test(Rating rating) {
                return rating.getRatee().getAppUser().getUsername().equals(ratee);
            }
        }).findFirst().orElse(null);

        return ((rating != null) ? rating.getRatingValue() : null);
    }

    @Override
    public Double getAverageRating(String username) {
        return profileRepository.findProfileByUsername(username).orElseThrow().getRating();
    }

    @Override
    public Double getAverageRating(Long id) {
        return profileRepository.findById(id).orElseThrow().getRating();
    }

    private void completeRating(Profile rater, Profile ratee, Double ratingValue) {
        ProfileRatingKey key = new ProfileRatingKey(rater.getId(), ratee.getId());
        ratingRepository.findById(key).ifPresentOrElse(
                rating -> {
                    log.info("Existing rating was found {}", rating.getId());
                    rating.setRatingValue(ratingValue);
                },
                () -> {
                    Rating rating = new Rating(key, rater, ratee, ratingValue);
                    log.info("This rater is rating the ratee for the first time {}", rating.getId());
                    ratee.getProfileRatings().add(rating);
                    ratingRepository.save(rating);
                }
        );
    }

    private void calculateRating(Profile ratee) {
        if (ratee.getProfileRatings().isEmpty()) return;

        double currentRatingSum = ratee.getProfileRatings()
                .stream()
                .map(Rating::getRatingValue)
                .reduce(0d, Double::sum);

        ratee.setRating(currentRatingSum / ratee.getProfileRatings().size());
    }
}
