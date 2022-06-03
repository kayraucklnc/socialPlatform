package com.segmentationfault.huceng.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRatingKey implements Serializable {
    @Column(name = "rater_id")
    private Long raterId; //the one who is rating
    @Column(name = "ratee_id")
    private Long rateeId; //the one who is being rated
}
