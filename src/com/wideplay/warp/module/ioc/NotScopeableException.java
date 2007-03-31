package com.wideplay.warp.module.ioc;

/**
 * Created with IntelliJ IDEA.
 * On: 23/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
class NotScopeableException extends RuntimeException {
    public NotScopeableException(String message) {
        super(message);
    }

    public NotScopeableException(String message, Throwable cause) {
        super(message, cause);
    }
}
