package com.segmentationfault.huceng.usecases.profile.service;

import com.segmentationfault.huceng.entity.Profile;
import com.segmentationfault.huceng.usecases.profile.dto.ProfileRequest;

public interface ProfileService {

    void createProfile(String username, ProfileRequest request);

    void updateProfile(String username, ProfileRequest request);

    void updateProfileBanner(String username, String banner);

    void updateProfilePicture(String username, String picture);

    void updateProfileAbout(String username, String about);

    void deleteProfile(String username);

    Profile getCurrentUserProfile(String username);

    Profile getProfileByUsername(String username);

    void updateRating(String username);

}
