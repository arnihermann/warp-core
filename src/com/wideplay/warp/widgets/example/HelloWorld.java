package com.wideplay.warp.widgets.example;

import com.wideplay.warp.widgets.At;
import com.wideplay.warp.widgets.Show;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@At("/hello")
public class HelloWorld {
    private String message = "Hello from warp-widgets!";

    public String getMessage() {
        return message;
    }
}