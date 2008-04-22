package com.wideplay.warp.widgets;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public @interface Switch {
    String value(); //parameter?
    SwitchKind kind() default SwitchKind.REQUEST_PARAMETER;

}
