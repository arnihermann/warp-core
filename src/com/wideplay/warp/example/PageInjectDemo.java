package com.wideplay.warp.example;

import com.google.inject.Inject;
import com.google.inject.cglib.proxy.Proxy;
import com.wideplay.warp.annotations.OnEvent;
import com.wideplay.warp.annotations.OnEvents;
import com.wideplay.warp.annotations.Page;
import com.wideplay.warp.module.pages.event.Redirect;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class PageInjectDemo {
    private String title = "This is the title!";

    private final Log log = LogFactory.getLog(PageInjectDemo.class);


    /**
     * dispatch @Fwd events to handlers in 'delegate' (if any)
     */
    @OnEvents(Fwd.class) 
    @Inject private EventDelegate delegate;

    /**
     *  inject 'Next' page without viral injection (except @Managed fields)
     */
    @Inject @Page private Next next;


    

    /**
     * An event handler for events published as @NextPage
     *
     * @return Returns the next page to send the user to,
     * which happens to be called "Next"
     */
    @OnEvent @NextPage
    public Next goToNextEventHandler() {
        //set counter on next page (should force instantiation of Next)
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
