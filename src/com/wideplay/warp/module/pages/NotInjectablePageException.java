package com.wideplay.warp.module.pages;

/**
 * Created with IntelliJ IDEA.
 * On: 21/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class NotInjectablePageException extends RuntimeException {

    public NotInjectablePageException(String message) {
        super(message);
    }

    public NotInjectablePageException(String message, Throwable cause) {
        super(message, cause);
    }
}
