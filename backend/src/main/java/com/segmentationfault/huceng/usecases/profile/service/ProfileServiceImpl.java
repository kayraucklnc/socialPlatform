package com.segmentationfault.huceng.usecases.profile.service;

import com.segmentationfault.huceng.entity.AppUser;
import com.segmentationfault.huceng.entity.Profile;
import com.segmentationfault.huceng.entity.repository.AppUserRepository;
import com.segmentationfault.huceng.entity.repository.ProfileRepository;
import com.segmentationfault.huceng.exception.ProfileAlreadyExistsException;
import com.segmentationfault.huceng.exception.ProfileDoesntExist;
import com.segmentationfault.huceng.usecases.profile.dto.ProfileRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final AppUserRepository appUserRepository;

    @Override
    public void createProfile(String username, ProfileRequest request) {
        AppUser appUser = appUserRepository.findAppUserByUsername(username).orElseThrow();
        if(appUser.getProfile() != null){
            log.info("Current AppUser: {}", appUser);
            Profile profile = new Profile(
                    null,
                    request.getProfile_picture(),
                    request.getBanner(),
                    request.getAbout(),
                    0.0,
                    appUser
            );
            log.info("Created Profile: {}", profile);
            profileRepository.save(profile);
        }else {
            throw new ProfileAlreadyExistsException(username);
        }
    }

    @Override
    public void updateProfile(String username, ProfileRequest request) {
        AppUser appUser = appUserRepository.findAppUserByUsername(username).orElseThrow();
        Profile profile = profileRepository.findById(appUser.getProfile().getId())
                .orElseThrow(() -> new ProfileDoesntExist(appUser.getUsername()));

        profile.setAbout(request.getAbout());
        profile.setProfilePicture(request.getProfile_picture());
        profile.setBanner(request.getBanner());
    }

    @Override
    public void updateProfileBanner(String username, String banner) {
        AppUser appUser = appUserRepository.findAppUserByUsername(username).orElseThrow();
        Profile profile = profileRepository.findById(appUser.getProfile().getId())
                .orElseThrow(() -> new ProfileDoesntExist(appUser.getUsername()));

        profile.setBanner(banner);
    }

    @Override
    public void updateProfilePicture(String username, String picture) {
        AppUser appUser = appUserRepository.findAppUserByUsername(username).orElseThrow();
        Profile profile = profileRepository.findById(appUser.getProfile().getId())
                .orElseThrow(() -> new ProfileDoesntExist(appUser.getUsername()));

        profile.setProfilePicture(picture);
    }

    @Override
    public void updateProfileAbout(String username, String about) {
        AppUser appUser = appUserRepository.findAppUserByUsername(username).orElseThrow();
        Profile profile = profileRepository.findById(appUser.getProfile().getId())
                .orElseThrow(() -> new ProfileDoesntExist(appUser.getUsername()));

        profile.setAbout(about);
    }

    @Override
    public void updateRating(String username) {
        AppUser appUser = appUserRepository.findAppUserByUsername(username).orElseThrow();
        //
    }

    @Override
    public void deleteProfile(String username) {
        Profile profile = profileRepository.findProfileByUsername(username)
                .orElseThrow(() -> new ProfileDoesntExist(username));
        log.info("Delete Profile - Current profile: {}", profile);
        profileRepository.delete(profile);
    }

    @Override
    public Profile getCurrentUserProfile(String username) {
        AppUser appUser = appUserRepository.findAppUserByUsername(username).orElseThrow();
        return profileRepository.findById(appUser.getProfile().getId()).orElseThrow();
    }

    @Override
    public Profile getProfileByUsername(String username) {
        return profileRepository.findProfileByUsername(username)
                .orElseThrow(() -> new ProfileDoesntExist(username));
    }

}
