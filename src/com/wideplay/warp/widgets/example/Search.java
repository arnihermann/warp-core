package com.wideplay.warp.widgets.example;

import com.wideplay.warp.widgets.At;
import com.wideplay.warp.widgets.Get;
import com.wideplay.warp.widgets.On;

import java.util.Collection;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@At("/wiki/search") @On("event")
public class Search {   //defaults to @Show("Search.xhtml"), or @Show("Search.html")

    private String query;   //"get" param
    private Collection<String> results;

    @Get("results")
    public void showResults() { //called after parameters are bound
//        results = dao.search(query);
    }


    //how about a search bar widget?
    @Get("widget")
    public void showSearchWidget() {
        //don't need to do anything but you could set up some contextual info on the widget here
    }
}
