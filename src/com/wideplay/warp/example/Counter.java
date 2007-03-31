package com.wideplay.warp.example;

import com.wideplay.warp.annotations.Managed;
import com.wideplay.warp.annotations.OnEvent;

/**
 * Created with IntelliJ IDEA.
 * On: 20/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class Counter {
    @Managed private int counter;

    public int getCounter() {
        return counter;
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
