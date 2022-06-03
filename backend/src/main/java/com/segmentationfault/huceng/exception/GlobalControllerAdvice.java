package com.segmentationfault.huceng.exception;

import com.segmentationfault.huceng.util.ErrorWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ResponseBody
    @ExceptionHandler(UsernameAlreadyExists.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorWrapper usernameAlreadyExitsHandler(UsernameAlreadyExists exception) {
        return new ErrorWrapper(409, exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(ProfileDoesntExist.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorWrapper profileDoesntExistHandler(ProfileDoesntExist exception) {
        return new ErrorWrapper(404, exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(ProfileAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorWrapper profileAlreadyExitsHandler(ProfileAlreadyExistsException exception) {
        return new ErrorWrapper(409, exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(PermissionDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorWrapper permissionDeniedHandler(PermissionDeniedException exception) {
        return new ErrorWrapper(403, exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(UserDoesntExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorWrapper userDoesntExist(UserDoesntExistException exception) {
        return new ErrorWrapper(403, exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(ReportDoesntExist.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorWrapper reportDoesntExist(ReportDoesntExist exception) {
        return new ErrorWrapper(403, exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(UserBannedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorWrapper userBanned(UserBannedException exception) {
        return new ErrorWrapper(403, exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(PastDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorWrapper userBanned(PastDateException exception) {
        return new ErrorWrapper(400, exception.getMessage());
    }
}
