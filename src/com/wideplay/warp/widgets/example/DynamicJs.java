package com.wideplay.warp.widgets.example;

import com.wideplay.warp.widgets.At;
import com.wideplay.warp.widgets.Show;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
@At("/dynamic.js") @Show("dynamic.js")
public class DynamicJs {
    public static final String A_MESSAGE = "Hi from warp-widgets! (this message was dynamically generated =)";

    public String getMessage() {
        return A_MESSAGE;
    }
}
