package com.wideplay.warp.example;

import com.wideplay.warp.annotations.Managed;
import com.wideplay.warp.annotations.OnEvent;
import com.wideplay.warp.annotations.OnEvents;
import com.wideplay.warp.annotations.Page;
import com.wideplay.warp.annotations.event.PreRender;
import com.google.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * On: 24/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class ViewportDemo {
    @Inject @Page @OnEvents Counter counter;
    @Inject Counter ajaxCounter;

    @OnEvent @PreRender
    void onBegin() {
        counter.increment();
        counter.increment();
        counter.increment();
        System.out.println(ViewportDemo.class.getName() + ".onBegin()!");
    }

    public Counter getCounter() {
        return counter;
    }

    public Counter getAjaxCounter() {
        return ajaxCounter;
    }
}
