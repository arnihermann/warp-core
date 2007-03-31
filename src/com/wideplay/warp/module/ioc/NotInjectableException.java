package com.wideplay.warp.module.ioc;

/**
 * Created with IntelliJ IDEA.
 * On: 25/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
class NotInjectableException extends RuntimeException {

    public NotInjectableException(String message) {
        super(message);
    }

    public NotInjectableException(String message, Throwable cause) {
        super(message, cause);
    }
}
