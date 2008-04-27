package com.wideplay.warp.widgets;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface On {
    String value(); //parameter?
    SwitchKind kind() default SwitchKind.REQUEST_PARAMETER;

}
