package com.wideplay.warp.annotations.event;

import com.wideplay.warp.annotations.OnEvent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by IntelliJ IDEA.
 * User: dprasanna
 * Date: 28/03/2007
 * Time: 15:23:29
 *
 *
 * An event resolution annotation provided by warp for event handler methods that wish
 * to be invoked BEFORE the page has been rendered by the Templater (but after state
 * is synchronized with the user's view of the page).
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@OnEvent
public @interface PreRender {
    static final String EVENT_ID = "@PreRender";
}
