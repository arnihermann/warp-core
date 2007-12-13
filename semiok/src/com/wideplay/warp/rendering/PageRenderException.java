package com.wideplay.warp.rendering;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
public class PageRenderException extends RuntimeException {

    public PageRenderException(String message) {
        super(message);
    }

    public PageRenderException(String message, Throwable cause) {
        super(message, cause);
    }
}
