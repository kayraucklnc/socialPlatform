package com.segmentationfault.huceng.util;

import lombok.Data;

/**
 * POJO to serialize an add role to user request object.
 */
@Data
public class AddRoleToUserRequest {
    private String username;
    private String roleName;
}
