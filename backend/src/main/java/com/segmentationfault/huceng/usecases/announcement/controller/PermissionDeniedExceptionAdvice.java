package com.segmentationfault.huceng.usecases.announcement.controller;

import com.segmentationfault.huceng.exception.PermissionDeniedException;
import com.segmentationfault.huceng.util.ErrorWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PermissionDeniedExceptionAdvice {
    //TODO: This global advice is at the wrong place, take a look at this

    @ResponseBody
    @ExceptionHandler(PermissionDeniedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorWrapper permissionDeniedExceptionHandler(PermissionDeniedException exception) {
        return new ErrorWrapper(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }
}