package com.wideplay.warp.annotations;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * <p>
 * This is used to tag methods that are event handlers for pages.
 * It should ALSO be used to tag custom annotations that are used to
 * group/distinguish event handlers.
 *
 * An event handler method MUST declare {@code @OnEvent} regardless of whether
 * or not it has other custom annotations for event disambiguation.
 * </p>
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.ANNOTATION_TYPE } )
@Documented
public @interface OnEvent {
}
