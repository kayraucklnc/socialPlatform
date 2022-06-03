package com.segmentationfault.huceng.usecases.rate.dto;

import lombok.Data;

@Data
public class RatingRequestByUsername {
    private final String username;
    private final Double rating;
}
