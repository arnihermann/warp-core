package com.wideplay.warp.widgets.rendering;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class Strings {
    private Strings() {}

    public static void nonEmpty(String aString, String message) {
        if (empty(aString))
            throw new IllegalArgumentException(message);
    }

    public static boolean empty(String string) {
        return null == string || "".equals(string.trim());

    }
}
