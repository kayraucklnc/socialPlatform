package com.segmentationfault.huceng.exception;

import java.util.Date;

public class UserBannedException extends RuntimeException {
    public UserBannedException(String username, String email) {
        super("User with username " + username + " has been banned.");
    }

    public UserBannedException(String username) {
        super("User " + username + " is indefinitely banned.");
    }

    public UserBannedException(String username, Date timeout) {
        super("User " + username + " is timed out. Expiry date: " + timeout);
    }
}
