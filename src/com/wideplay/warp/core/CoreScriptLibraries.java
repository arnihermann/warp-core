package com.wideplay.warp.core;

import com.wideplay.warp.rendering.ScriptLibrary;

/**
 * Created with IntelliJ IDEA.
 * On: 27/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
enum CoreScriptLibraries implements ScriptLibrary {
    YUI_MIN("http://yui.yahooapis.com/2.2.0/build/yahoo/yahoo-min.js"),
    YUI_EVENT_MIN("http://yui.yahooapis.com/2.2.0/build/event/event-min.js"),
    YUI_DOM_EVENT("http://yui.yahooapis.com/2.2.0/build/yahoo-dom-event/yahoo-dom-event.js"),
    YUI_DRAGDROP_MIN("http://yui.yahooapis.com/2.2.0/build/dragdrop/dragdrop-min.js"),
    ;



    private String libraryURL;
    private CoreScriptLibraries(String libraryURL) {
        this.libraryURL = libraryURL;
    }

    public String getLibraryURL() {
        return libraryURL;
    }
}
