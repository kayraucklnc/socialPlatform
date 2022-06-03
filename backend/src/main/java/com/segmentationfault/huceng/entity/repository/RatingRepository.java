package com.segmentationfault.huceng.entity.repository;

import com.segmentationfault.huceng.entity.ProfileRatingKey;
import com.segmentationfault.huceng.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, ProfileRatingKey> {

}
