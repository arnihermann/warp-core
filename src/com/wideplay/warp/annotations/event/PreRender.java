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
 * <p/>
 * TODO: Describe me!
 *
 * @author dprasanna
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@OnEvent
public @interface PreRender {
    static final String EVENT_ID = "@PreRender";
}
