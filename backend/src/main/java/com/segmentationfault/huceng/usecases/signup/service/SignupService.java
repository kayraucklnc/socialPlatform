package com.segmentationfault.huceng.usecases.signup.service;

import com.segmentationfault.huceng.entity.PendingUser;
import com.segmentationfault.huceng.usecases.signup.dto.PendingUserResponse;

import java.util.List;

public interface SignupService {

    PendingUser saveUserAsPending(PendingUser pendingUser);

    void approveUser(String username);
    void denyUser(String username);

    void assignRoleToPendingUser(String username, String roleName);

    List<PendingUserResponse> getAllPendingUsers();

}
