package com.wideplay.warp.widgets.example;

import com.wideplay.warp.widgets.At;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@At("/embed")
public class Embed {
    private String arg = "Embedding in warp-widgets is awesome!";

    public String getArg() {
        return arg;
    }
}