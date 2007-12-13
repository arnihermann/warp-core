package com.wideplay.warp.example;

import com.google.inject.Inject;
import com.wideplay.warp.annotations.OnEvent;
import com.wideplay.warp.module.pages.event.Forward;

/**
 * Created with IntelliJ IDEA.
 * On: 22/03/2007
 *
 * NOT a page object
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
public class EventDelegate {
    @Inject private Next next;

    @OnEvent @Fwd
    public Object forward() {
        //set counter on next page
        next.setNumber(100);

        System.out.println(getClass() + " event handled!");

        return Forward.to(next);
    }

}
