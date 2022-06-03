package com.segmentationfault.huceng.exception;

public class ProfileAlreadyExistsException extends RuntimeException{
    public ProfileAlreadyExistsException(String username) {
        super("User " + username + " already have a profile.");
    }

}
