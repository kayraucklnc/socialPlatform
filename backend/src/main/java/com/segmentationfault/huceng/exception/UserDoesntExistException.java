package com.segmentationfault.huceng.exception;

public class UserDoesntExistException extends RuntimeException{
    public UserDoesntExistException(String username) {
        super("User with username " + username + " does not exist.");
    }
}
