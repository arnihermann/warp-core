package com.wideplay.warp.util.beans;

/**
 * Created with IntelliJ IDEA.
 * On: 25/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
class NotReadablePropertyException extends RuntimeException {

    public NotReadablePropertyException(String message) {
        super(message);
    }

    public NotReadablePropertyException(String message, Throwable cause) {
        super(message, cause);
    }
}
