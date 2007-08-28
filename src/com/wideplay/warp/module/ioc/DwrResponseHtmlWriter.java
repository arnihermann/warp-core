package com.wideplay.warp.module.ioc;

import com.wideplay.warp.rendering.AbstractHtmlWriter;
import com.wideplay.warp.rendering.ScriptEvents;
import com.wideplay.warp.rendering.ScriptLibrary;

/**
 * Created with IntelliJ IDEA.
 * On: 27/08/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class DwrResponseHtmlWriter extends AbstractHtmlWriter {

    public String getBuffer() {
        return getWriter().toString();
    }

    public void registerEvent(String elementName, ScriptEvents event, String annotation, int topicId) {
        //TODO register event in a script that runs afterward
    }


    public void registerAsyncEvent(String id, ScriptEvents click, String encodedEvent, int topicId, String[] viewports) {
        //TODO register event in a script that runs afterward
    }

    public void registerScriptLibrary(ScriptLibrary library) {
        //can't do this in responding viewports
    }

    public void writeToOnLoad(String text) {
        //can't do this in responding viewports
    }
}
