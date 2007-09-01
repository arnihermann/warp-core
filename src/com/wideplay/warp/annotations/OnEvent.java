package com.wideplay.warp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * This is used to tag methods that are event handlers for pages.
 * It should ALSO be used to tag custom annotations that are used to
 * group/distinguish event handlers.
 *
 * An event handler method MUST declare @OnEvent regardless of whether
 * or not it has other custom annotations for event disambiguation.
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.ANNOTATION_TYPE } )
public @interface OnEvent {
}
