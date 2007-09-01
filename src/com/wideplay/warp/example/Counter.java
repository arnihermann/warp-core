package com.wideplay.warp.example;

import com.google.inject.Singleton;
import com.wideplay.warp.annotations.Managed;
import com.wideplay.warp.annotations.OnEvent;

/**
 * Created with IntelliJ IDEA.
 * On: 20/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
@Singleton
public class Counter {
    @Managed private int counter;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    @OnEvent
    public void increment() {
        counter++;
    }

    @OnEvent @Fwd
    public void reset() {
        counter = -1;
    }
}
