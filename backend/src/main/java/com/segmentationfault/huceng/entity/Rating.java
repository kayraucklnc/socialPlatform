package com.segmentationfault.huceng.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating {

    @EmbeddedId
    private ProfileRatingKey id = new ProfileRatingKey();

    @ManyToOne
    @MapsId("raterId")
    @JoinColumn(name = "rater_id")
    @NonNull private Profile rater;

    @ManyToOne
    @MapsId("rateeId")
    @JoinColumn(name = "ratee_id")
    @NonNull private Profile ratee;

    @NonNull private Double ratingValue;
}
