package com.wideplay.warp.widgets.example;

import com.google.inject.name.Named;
import com.wideplay.warp.widgets.At;
import com.wideplay.warp.widgets.Get;
import com.wideplay.warp.widgets.Show;
import com.wideplay.warp.widgets.rendering.EmbedAs;
import com.wideplay.warp.widgets.resources.Assets;
import com.wideplay.warp.widgets.resources.Export;

/**
 *
 */
@At("/wiki/page/:title")
@Show("Wiki.html") 
@EmbedAs("Wiki")
@Assets({@Export(at = "/your.js", resource = "your.js")})
public class Wiki {
    private String title;
    private String language;    //"get" variable, bound by request parameter of same name, via setter
    private String text;        //"post" variable, bound similarly
    private int counter;

    @Get
    public void showPage(@Named("title") String title) {    //URI-part extraction
//        this.title = wikiFind.fetch(title);
        //etc.

        //page is now rendered with the default view
    }

    public int getCounter() {
        return counter;
    }
}
