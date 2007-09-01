package com.wideplay.warp.example;

import com.wideplay.warp.annotations.Managed;
import com.wideplay.warp.annotations.OnEvent;

/**
 * Created with IntelliJ IDEA.
 * On: 24/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
public class PanelDemo {
    //panel is visible by default
    @Managed private boolean visible = true;
    private String text = "text inside dropzone";


    @OnEvent
    public void toggleVisibility() {
        visible = !visible;
    }

    @OnEvent @Fwd
    public void drop() {
        text = "text has changed!";
    }

    //getters/setters-------
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }


    public String getText() {
        return text;
    }
}
