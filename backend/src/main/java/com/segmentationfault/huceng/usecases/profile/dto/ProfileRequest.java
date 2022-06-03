package com.segmentationfault.huceng.usecases.profile.dto;

import lombok.Data;

@Data
public class ProfileRequest {
    private String profile_picture;
    private String banner;
    private String about;

    public ProfileRequest(String profile_picture, String banner, String about) {
        this.profile_picture = profile_picture;
        this.banner = banner;
        this.about = about;
    }
}
