package com.segmentationfault.huceng.util;

import lombok.Data;

/**
 * POJO to serialize a login request object.
 */
@Data
public class LoginRequest {
    private String username;
    private String password;
}
