package com.wideplay.warp.components.core;

import com.wideplay.warp.rendering.ScriptLibrary;

/**
 * Created with IntelliJ IDEA.
 * On: 27/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public enum CoreScriptLibraries implements ScriptLibrary {

    DWR_ENGINE("/dwr/engine.js"),
    DWR_UTIL("/dwr/util.js"),
    DWR_REMOTE_EVENT_PROXY("/dwr/interface/RemoteEventProxy.js"),
    ;

    private String libraryURL;
    private CoreScriptLibraries(String libraryURL) {
        this.libraryURL = libraryURL;
    }

    public String getLibraryURL() {
        return libraryURL;
    }
}
