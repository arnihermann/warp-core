package com.wideplay.warp.widgets.rendering;

import java.lang.annotation.*;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface With {
    Class<? extends Annotation>[] value();
}