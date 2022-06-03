package com.segmentationfault.huceng.usecases.requestUserInfo.service;

import com.segmentationfault.huceng.entity.AppUser;
import com.segmentationfault.huceng.entity.Profile;
import com.segmentationfault.huceng.entity.repository.AppUserRepository;
import com.segmentationfault.huceng.entity.repository.ProfileRepository;
import com.segmentationfault.huceng.usecases.requestUserInfo.dto.ProfileSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RequestUserServiceImpl implements RequestUserService {
    private final ProfileRepository profileRepository;
    private final AppUserRepository appUserRepository;

    @Override
    public ProfileSummary getProfileSummary(String username) {

        Profile profile = profileRepository.findProfileByUsername(username).orElseThrow();
        AppUser appUser = appUserRepository.findAppUserByUsername(username).orElseThrow();
        ProfileSummary summary = new ProfileSummary(appUser.getFirstName(), appUser.getLastName(),appUser.getEmail(), appUser.getUsername(), profile.getAbout(), profile.getRating(), appUser.getPosts());

        return summary;
    }

    @Override
    public Page<AppUser> getAllUsers(int page, int size) {
        Pageable firstPageWithTwoElements = PageRequest.of(page, size, Sort.by("username"));
        Page<AppUser> allProducts = appUserRepository.findAll(firstPageWithTwoElements);

        return allProducts;
    }
}
