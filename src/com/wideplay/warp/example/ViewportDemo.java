package com.wideplay.warp.example;

import com.wideplay.warp.annotations.Managed;
import com.wideplay.warp.annotations.OnEvent;
import com.wideplay.warp.annotations.OnEvents;
import com.wideplay.warp.annotations.Page;
import com.google.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * On: 24/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class ViewportDemo {
    @Inject @OnEvents Counter counter;

    public Counter getCounter() {
        return counter;
    }
}
