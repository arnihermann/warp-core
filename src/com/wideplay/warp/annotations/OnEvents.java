package com.wideplay.warp.annotations;

import java.lang.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: dprasanna
 * Date: 22/03/2007
 * Time: 10:35:53
 *
 * <p>
 *
 * Used to designate objects that are event handling delegates. Arguments
 * to this annotation will be used to filter events dispatched to the delegate.
 *
 * for example:
 * </p>
 *  <pre>
 *      @OnEvents(EventTypeA.class) private MyHandler eventDelegate;
 *  </pre>
 *<p>
 * Only events published to @EventTypeA will be dispatched to the delegate. If no
 * arguments are specified, ALL events are candidates for dispatch to the delegate
 * (provided it has suitable handlers).
 *
 * Event handlers present in a page class are dispatched before delegates' event handlers
 * regardless of annotations.
 *</p>
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( ElementType.FIELD )
@Documented
public @interface OnEvents {
    Class<? extends Annotation>[] value() default { };
}
