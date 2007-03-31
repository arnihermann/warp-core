package com.wideplay.warp.module.ioc;

/**
 * Created with IntelliJ IDEA.
 * On: 25/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
class RequestBindingException extends RuntimeException {

    public RequestBindingException(String message) {
        super(message);
    }

    public RequestBindingException(String message, Throwable cause) {
        super(message, cause);
    }
}
