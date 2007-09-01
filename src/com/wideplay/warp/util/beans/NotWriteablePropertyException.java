package com.wideplay.warp.util.beans;

/**
 * Created with IntelliJ IDEA.
 * On: 18/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
class NotWriteablePropertyException extends RuntimeException {

    public NotWriteablePropertyException(String message) {
        super(message);
    }

    public NotWriteablePropertyException(String message, Throwable cause) {
        super(message, cause);
    }
}
