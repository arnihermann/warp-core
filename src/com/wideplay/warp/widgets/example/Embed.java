package com.wideplay.warp.widgets.example;

import com.wideplay.warp.widgets.At;
import com.wideplay.warp.widgets.Show;

import java.util.List;
import java.util.Arrays;

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