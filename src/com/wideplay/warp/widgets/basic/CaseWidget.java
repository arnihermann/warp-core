package com.wideplay.warp.widgets.basic;

import com.wideplay.warp.widgets.rendering.CallWith;
import com.wideplay.warp.widgets.rendering.EmbedAs;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
@EmbedAs("Case") @CallWith("When")
public class CaseWidget {
    private Object choice;

    public Object getChoice() {
        return choice;
    }

    public void setChoice(Object choice) {
        this.choice = choice;
    }
}
