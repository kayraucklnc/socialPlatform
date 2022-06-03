package com.segmentationfault.huceng.usecases.signup.dto;

import lombok.Data;

@Data
public class PendingUserRequest {
    private String first_name;
    private String last_name;
    private String email;
    private String username;
    private String password;
    private String role;
}

