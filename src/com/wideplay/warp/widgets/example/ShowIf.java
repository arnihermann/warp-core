package com.wideplay.warp.widgets.example;

import com.wideplay.warp.widgets.At;
import com.wideplay.warp.widgets.Show;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@At("/showif")
public class ShowIf {
    private boolean show;
    private String message = "Hello from warp-widgets!";

    public String getMessage() {
        return message;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}