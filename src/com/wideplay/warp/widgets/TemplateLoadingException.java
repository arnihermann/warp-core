package com.wideplay.warp.widgets;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
class TemplateLoadingException extends RuntimeException {
    public TemplateLoadingException(String msg, Exception e) {
        super(msg, e);
    }

    public TemplateLoadingException(String msg) {
        super(msg);
    }
}
