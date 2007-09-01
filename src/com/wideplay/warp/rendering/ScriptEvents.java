package com.wideplay.warp.rendering;

/**
 * Created with IntelliJ IDEA.
 * On: 24/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
public enum ScriptEvents {
    CLICK("click"),
    DOUBLE_CLICK("dblclick"),
    CHANGE("CHANGE")
    ;

    private String value;

    private ScriptEvents(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
