package com.segmentationfault.huceng.usecases.signup.controller;

import com.segmentationfault.huceng.entity.PendingUser;
import com.segmentationfault.huceng.usecases.signup.dto.ApproveUserRequest;
import com.segmentationfault.huceng.usecases.signup.dto.PendingUserRequest;
import com.segmentationfault.huceng.usecases.signup.dto.PendingUserResponse;
import com.segmentationfault.huceng.usecases.signup.service.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/signup") //localhost::api/signup
@RequiredArgsConstructor
public class SignupController {
    private final SignupService signupService;

    @PostMapping
    public void savePendingUser(@RequestBody PendingUserRequest pendingUserRequest) {
        PendingUser user = new PendingUser(
                null,
                pendingUserRequest.getFirst_name(),
                pendingUserRequest.getLast_name(),
                pendingUserRequest.getEmail(),
                pendingUserRequest.getUsername(),
                pendingUserRequest.getPassword(),
                new ArrayList<>()
        );
        signupService.saveUserAsPending(user);
        signupService.assignRoleToPendingUser(user.getUsername(), pendingUserRequest.getRole());
    }

    @PostMapping("/approve") //api/signup/approve
    public void approveUser(@RequestBody ApproveUserRequest approveUserRequest) {
        signupService.approveUser(approveUserRequest.getUsername());
    }

    @PostMapping("/deny") //api/signup/approve
    public void denyUser(@RequestBody ApproveUserRequest approveUserRequest) {
        signupService.denyUser(approveUserRequest.getUsername());
    }

    @GetMapping("")
    public List<PendingUserResponse> getPendingUsers() {
        return signupService.getAllPendingUsers();
    }
}
