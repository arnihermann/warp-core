package com.wideplay.warp.example;

import com.wideplay.warp.annotations.Managed;
import com.wideplay.warp.annotations.OnEvent;

/**
 * Created with IntelliJ IDEA.
 * On: 24/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class EventsDemo {
    private String[] beans = new String[] {"text1","text2","text3","text4"};
    private String message = "none";

    @OnEvent
    public void topicalHandler(String message) {
        this.message = message;
        System.out.println(message);
    }


    public String[] getBeans() {
        return beans;
    }

    public String getMessage() {
        return message;
    }
}
