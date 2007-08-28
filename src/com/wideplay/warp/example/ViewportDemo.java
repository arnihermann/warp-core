package com.wideplay.warp.example;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.wideplay.warp.annotations.OnEvent;
import com.wideplay.warp.annotations.OnEvents;
import com.wideplay.warp.annotations.Page;
import com.wideplay.warp.annotations.event.PreRender;

/**
 * Created with IntelliJ IDEA.
 * On: 24/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
@Singleton
public class ViewportDemo {
    @OnEvents Counter counter;

    @Inject
    public ViewportDemo(@Page Counter counter) {
        this.counter = counter;
        counter.increment();
        counter.increment();
        counter.increment();
    }

    @OnEvent @PreRender
    void onBegin() {
        System.out.println(ViewportDemo.class.getName() + ".onBegin()!");
    }

    public Counter getCounter() {
        return counter;
    }


    public void setCounter(int counter) {
        this.counter.setCounter(counter);
    }
}
