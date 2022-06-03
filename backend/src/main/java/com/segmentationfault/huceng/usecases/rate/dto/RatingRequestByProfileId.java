package com.segmentationfault.huceng.usecases.rate.dto;

import lombok.Data;

@Data
public class RatingRequestByProfileId {
    private final Long profileId;
    private final Double rating;
}
