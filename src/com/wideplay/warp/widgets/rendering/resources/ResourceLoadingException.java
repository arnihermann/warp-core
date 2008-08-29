package com.wideplay.warp.widgets.rendering.resources;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
class ResourceLoadingException extends RuntimeException {
    public ResourceLoadingException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ResourceLoadingException(String msg) {
        super(msg);
    }
}
