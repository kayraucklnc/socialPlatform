package com.segmentationfault.huceng.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table
@Data
@AllArgsConstructor
@Slf4j
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String profilePicture;
    private String banner;
    private String about;
    private Double rating;

    public Profile() {
        this.profilePicture = "./assets/person/default.png";
        this.banner = "./assets/banner/default.jpg";
        this.about = "Hey, this looks empty!";
        this.rating = Double.valueOf(0);
    }

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private AppUser appUser;

    @OneToMany(mappedBy = "rater")
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private final Collection<Rating> ratedProfiles = new ArrayList<>();

    @OneToMany(mappedBy = "ratee")
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private final Collection<Rating> profileRatings = new ArrayList<>();
}
