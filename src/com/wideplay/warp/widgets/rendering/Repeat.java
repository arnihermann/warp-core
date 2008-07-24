package com.wideplay.warp.widgets.rendering;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public @interface Repeat {
    String VAR = "var";
    String PAGE_VAR = "pageVar";
    String ITEMS = "items";

    String items();
    String var() default "__this";
    String pageVar() default "__page";
}
