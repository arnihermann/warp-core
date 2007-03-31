package com.wideplay.warp.example;

import com.wideplay.warp.annotations.event.PreRender;
import com.wideplay.warp.annotations.OnEvent;

/**
 * Created with IntelliJ IDEA.
 * On: 24/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class Start {
    @OnEvent @PreRender
    public void pre() {
        System.out.println("Before render lifecycle event!");
    }
}
