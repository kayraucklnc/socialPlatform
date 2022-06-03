package com.segmentationfault.huceng.exception;

public class ProfileDoesntExist extends RuntimeException {
    public ProfileDoesntExist(String username) {
        super("User " + username + " does not have a profile yet.");
    }

    public ProfileDoesntExist(Long id) {
        super("No profile with ID: "+ id + "was found.");
    }
}
