package com.wideplay.warp.example;

import com.wideplay.warp.annotations.OnEvent;
import com.wideplay.warp.annotations.Page;
import com.wideplay.warp.module.pages.event.Forward;
import com.google.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * On: 22/03/2007
 *
 * NOT a page object
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class EventDelegate {
    @Inject @Page private Next next;

    @OnEvent @Fwd
    public Object forward() {
        //set counter on next page
        next.setNumber(100);

        System.out.println(getClass() + " event handled!");

        return Forward.to(next);
    }

}
