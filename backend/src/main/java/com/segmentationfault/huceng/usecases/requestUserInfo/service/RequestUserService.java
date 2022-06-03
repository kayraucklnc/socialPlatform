package com.segmentationfault.huceng.usecases.requestUserInfo.service;

import com.segmentationfault.huceng.entity.AppUser;
import com.segmentationfault.huceng.usecases.requestUserInfo.dto.ProfileSummary;
import org.springframework.data.domain.Page;

public interface RequestUserService {

    ProfileSummary getProfileSummary(String username);

    Page<AppUser> getAllUsers(int page, int size);
}
