package com.wideplay.warp.internal.pages;

import com.google.inject.Inject;
import com.wideplay.warp.annotations.OnEvent;
import com.wideplay.warp.annotations.OnEvents;
import com.wideplay.warp.example.EventDelegate;
import com.wideplay.warp.example.Fwd;
import com.wideplay.warp.example.Next;
import com.wideplay.warp.example.NextPage;
import com.wideplay.warp.module.pages.event.Redirect;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
public class PCRBTestPage {
    private String title = "This is the title!";


    /**
     * dispatch @Fwd events to handlers in 'delegate' (if any)
     */
    @OnEvents(Fwd.class)
    @Inject
    private EventDelegate delegate;

    /**
     *  inject 'Next' page without viral injection (except @Managed fields)
     */
    @Inject
    private Next next;




    /**
     * An event handler for events published as @NextPage
     *
     * @return Returns the next page to send the user to,
     * which happens to be called "Next"
     */
    @OnEvent
    @NextPage
    public Next goToNextEventHandler() {
        //set counter on next page
        next.setNumber(50);

        //redirect to 'next' page (unless overriden by another handler-which it's not =)
        return next;
    }




    /**
     * An event handler for global page events (published to no particular annotation)
     *
     * @return Returns the next page to send the user to,
     * which happens to be an external website
     */
    @OnEvent
    public Object globalEventHandler() {
        return Redirect.to("http://www.wideplay.com");  //interrupt event handling and redirect
    }



    //getters/setters---------

    /**
     * A Read-only property (because it has NO corresponding setter),
     * of the page that can be referenced in the template as
     *
     * ${hello}
     *
     * @return Returns JavaBean property "hello"
     */
    public String getHello() {
        return "hello there!";
    }


    /**
     * A Read-write property (because it HAS a corresponding setter),
     * of the page that can be referenced in the template as:
     *
     * ${title}
     *
     * @return Returns JavaBean property "title"
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
