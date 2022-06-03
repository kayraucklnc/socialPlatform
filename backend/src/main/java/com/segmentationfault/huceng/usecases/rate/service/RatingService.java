package com.segmentationfault.huceng.usecases.rate.service;

public interface RatingService {

    Double rateProfileByProfileId(Long raterId, Long rateeId, Double ratingValue);

    Double rateProfileByUsername(String raterUsername, String rateeUsername, Double ratingValue);

    Double getRatingOfUserGivenByUser(String rater, String ratee);

    Double getAverageRating(String username);

    Double getAverageRating(Long id);
}
