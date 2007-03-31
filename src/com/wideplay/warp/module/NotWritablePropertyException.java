package com.wideplay.warp.module;

/**
 * Created with IntelliJ IDEA.
 * On: 18/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class NotWritablePropertyException extends RuntimeException {

    public NotWritablePropertyException(String message) {
        super(message);
    }

    public NotWritablePropertyException(String message, Throwable cause) {
        super(message, cause);
    }
}
