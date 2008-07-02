package com.wideplay.warp.widgets.rendering;

import java.lang.annotation.*;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Embed {
    Class<? extends Annotation> value();
}