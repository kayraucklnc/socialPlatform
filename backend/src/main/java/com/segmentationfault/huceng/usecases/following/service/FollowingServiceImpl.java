package com.segmentationfault.huceng.usecases.following.service;

import com.segmentationfault.huceng.entity.AppUser;
import com.segmentationfault.huceng.entity.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FollowingServiceImpl implements FollowingService {
    private final AppUserRepository appUserRepository;

    @Override
    public boolean checkUserFollow(AppUser targetUser) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = appUserRepository.findAppUserByUsername(auth.getName()).orElseThrow();

        return checkUserFollow(appUser, targetUser);
    }

    @Override
    public void followUser(AppUser user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = appUserRepository.findAppUserByUsername(auth.getName()).orElseThrow();

        followUser(appUser, user);
    }

    @Override
    public void unfollowUser(AppUser user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = appUserRepository.findAppUserByUsername(auth.getName()).orElseThrow();

        unfollowUser(appUser, user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkUserFollow(AppUser user, AppUser targetUser) {
        if (user.equals(targetUser)) { return false; }

        Collection<AppUser> checkInList;
        AppUser checkUser;
        Collection<AppUser> domainFollowed = user.getFollowedUsers();
        Collection<AppUser> targetFollowers = targetUser.getFollowers();

        if (domainFollowed.size() > targetFollowers.size()) {
            checkInList = targetFollowers;
            checkUser = user;
        } else {
            checkInList = domainFollowed;
            checkUser = targetUser;
        }

        return checkInList.contains(checkUser);
    }

    @Override
    public void followUser(AppUser domainUser, AppUser targetUser) {
        if (checkUserFollow(domainUser, targetUser) || domainUser.equals(targetUser)) {
            return;
        }

        domainUser.getFollowedUsers().add(targetUser);
        targetUser.getFollowers().add(domainUser);
        appUserRepository.save(domainUser);
        appUserRepository.save(targetUser);
    }

    @Override
    public void unfollowUser(AppUser domainUser, AppUser targetUser) {
        if (!checkUserFollow(domainUser, targetUser)) {
            return;
        }

        domainUser.getFollowedUsers().remove(targetUser);
        targetUser.getFollowers().remove(domainUser);
        appUserRepository.save(domainUser);
        appUserRepository.save(targetUser);
    }
}
