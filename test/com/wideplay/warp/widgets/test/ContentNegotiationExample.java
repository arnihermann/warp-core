package com.wideplay.warp.widgets.test;

import com.wideplay.warp.widgets.At;
import com.wideplay.warp.widgets.Get;
import com.wideplay.warp.widgets.On;
import com.wideplay.warp.widgets.Show;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@At("/aPage") @Show("Wiki.html") @On("Accept")
public class ContentNegotiationExample {
    private int counter;

    @Get("text/plain") @Show("text.txt")    //Does not work yet!!!
    public void textPage() {

    }

    @Get("text/html") @Show("text.html")    //Does not work yet!!!
    public void htmlPage() {

    }

    public int getCounter() {
        return counter;
    }
}
