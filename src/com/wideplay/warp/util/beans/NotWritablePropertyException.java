package com.wideplay.warp.util.beans;

/**
 * Created with IntelliJ IDEA.
 * On: 18/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
class NotWritablePropertyException extends RuntimeException {

    public NotWritablePropertyException(String message) {
        super(message);
    }

    public NotWritablePropertyException(String message, Throwable cause) {
        super(message, cause);
    }
}
