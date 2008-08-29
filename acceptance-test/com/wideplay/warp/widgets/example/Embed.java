package com.wideplay.warp.widgets.example;

import com.wideplay.warp.widgets.At;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@At("/embed")
public class Embed {
    public static final String MESSAGE = "Embedding in warp-widgets is awesome!";
    
    private String arg = MESSAGE;

    public String getArg() {
        return arg;
    }
}