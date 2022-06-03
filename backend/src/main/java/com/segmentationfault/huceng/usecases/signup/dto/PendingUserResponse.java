package com.segmentationfault.huceng.usecases.signup.dto;

import com.segmentationfault.huceng.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor
public class PendingUserResponse {
    private String username;
    private String email;
    private String role;
}
