package com.signaretech.seneachat.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception should be thrown for application error that are not checked.
 * It returns a 400 response code along with the error message.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SeneachatErrorException extends RuntimeException {

    public SeneachatErrorException(String message) {
        super(message);
    }

    public SeneachatErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeneachatErrorException(Throwable cause) {
        super(cause);
    }

    protected SeneachatErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
