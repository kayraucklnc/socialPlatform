package com.segmentationfault.huceng.usecases.requestUserInfo.controller;

import com.segmentationfault.huceng.entity.AppUser;
import com.segmentationfault.huceng.usecases.post.dto.PostRequest;
import com.segmentationfault.huceng.usecases.requestUserInfo.dto.PagingRequest;
import com.segmentationfault.huceng.usecases.requestUserInfo.dto.ProfileSummary;
import com.segmentationfault.huceng.usecases.requestUserInfo.service.RequestUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = "/api/requestinfo")
@RequiredArgsConstructor
@Slf4j
public class RequestUserInfoController {
    private final RequestUserService requestUserService;

    @GetMapping("/{username}")
    public ProfileSummary getProfileSummary(@PathVariable String username) {
        return requestUserService.getProfileSummary(username);
    }

    @PostMapping("/all")
    public Page<AppUser> getAllUsers(@RequestBody PagingRequest pagingRequest) {
        return requestUserService.getAllUsers(pagingRequest.getPage(), pagingRequest.getSize());
    }

}
