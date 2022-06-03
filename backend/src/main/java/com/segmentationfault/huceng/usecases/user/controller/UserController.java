package com.segmentationfault.huceng.usecases.user.controller;

import com.segmentationfault.huceng.entity.AppUser;
import com.segmentationfault.huceng.startup.StartupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/user") //localhost::api/user
@RequiredArgsConstructor
public class UserController {
    private final StartupService startupService;

    @GetMapping("{userID}")
    public AppUser getUserByID(@PathVariable Long userID) {
        return startupService.getUser(userID);
    }

    @GetMapping("username/{username}")
    public AppUser getUserByUsername(@PathVariable String username) {
        return startupService.getUser(username);
    }
}
