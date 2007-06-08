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
 * Time: 15:23:41
 * <p/>
 * 
 * An event resolution annotation provided by warp for event handler methods that wish
 * to be invoked AFTER the page has been rendered by the TemplatingFilter (not guaranteed to be
 * at the client browser).
 *
 * @author dprasanna
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@OnEvent
public @interface PostRender {
    static final String EVENT_ID = "@PostRender";
}
