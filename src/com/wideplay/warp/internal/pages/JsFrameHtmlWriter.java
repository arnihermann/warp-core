package com.wideplay.warp.internal.pages;

import com.wideplay.warp.components.core.CoreScriptLibraries;
import static com.wideplay.warp.internal.pages.JsSupportUtils.*;
import com.wideplay.warp.module.ioc.RemoteEventProxy;
import com.wideplay.warp.rendering.*;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
class JsFrameHtmlWriter extends AbstractHtmlWriter {
    private final StringBuilder onFrameLoadWriter = new StringBuilder();
    private final Set<String> linkedScripts = new LinkedHashSet<String>();

    public void registerScriptLibrary(ScriptLibrary library) {
        linkedScripts.add(library.getLibraryURL());
    }

    public void registerEvent(String elementName, ScriptEvents event, String annotation, int topicId) {
        onFrameLoadWriter.append("document.getElementById(\"");
        onFrameLoadWriter.append(elementName);
        onFrameLoadWriter.append("\").onclick=function(){ __warpForm.w_event.value= \"");
        onFrameLoadWriter.append(annotation);

        if (0 != topicId) {
            onFrameLoadWriter.append("\"; __warpForm.w_event_topic.value= \"");
            onFrameLoadWriter.append(topicId);
        }
        onFrameLoadWriter.append("\"; __warpForm.submit(); return false;}; ");
    }


    public void registerAsyncEvent(String id, ScriptEvents click, String encodedEvent, int topicId, String[] viewports) {

        //setup dwr engine and interface
        registerScriptLibrary(CoreScriptLibraries.DWR_REMOTE_EVENT_PROXY);
        registerScriptLibrary(CoreScriptLibraries.DWR_ENGINE);
        registerScriptLibrary(CoreScriptLibraries.DWR_UTIL);

        //write event trigger function
        onFrameLoadWriter.append(String.format(getScriptTemplate(DWR_EVENT_DISPATCH_FUNC),
                id,
                viewports[0], RemoteEventProxy.WARP_TARGET_PAGE_URI,

                RequestBinder.EVENT_PARAMETER_NAME, encodedEvent,
                RequestBinder.EVENT_TOPIC_PARAMETER_NAME, topicId,

                viewports[0]));
    }

    //write raw text to the body load js func
    public void writeToOnLoad(String text) {
        onFrameLoadWriter.append(text);
    }

    public String getBuffer() {
        //insert the onFrameLoadWriter content in the placeholder
        return getWriter()
                .toString()
                .replaceFirst(HtmlWriter.LINKED_SCRIPTS_PLACEHOLDER, wrapLinkedScripts(linkedScripts, getRequest().getContextPath()))
                .replaceFirst(HtmlWriter.ON_FRAME_LOAD_PLACEHOLDER, wrapOnFrameLoadFn(onFrameLoadWriter));
    }

}
