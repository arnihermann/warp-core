package com.wideplay.warp.widgets.example;

import com.wideplay.warp.widgets.At;
import com.wideplay.warp.widgets.rendering.EmbedAs;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@At("/hello")
@EmbedAs("Hello")   //exposes this object as @Hello widget usable in templates
public class HelloWorld {
    private String message = "Hello from warp-widgets!";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}