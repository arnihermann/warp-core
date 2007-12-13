package com.wideplay.warp.internal.pages;

import com.wideplay.warp.components.core.CoreScriptLibraries;
import static com.wideplay.warp.internal.pages.JsSupportUtils.*;
import com.wideplay.warp.rendering.*;
import com.wideplay.warp.util.TextTools;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
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

    public void registerInputBinding(String id) {
//        throw new UnsupportedOperationException();
    }

    public void registerAsyncEvent(String id, ScriptEvents click, String encodedEvent, int topicId, String[] viewports) {

        //setup dwr engine and interface
        registerScriptLibrary(CoreScriptLibraries.DWR_ENGINE);
        registerScriptLibrary(CoreScriptLibraries.DWR_UTIL);

        //write event trigger function
        onFrameLoadWriter.append(String.format(getScriptTemplate(DWR_EVENT_DISPATCH_FUNC),
                id,

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
        final String contextPath = getRequest().getContextPath();
        final String html = getWriter()
                .toString()
                .replaceFirst(HtmlWriter.LINKED_SCRIPTS_PLACEHOLDER, wrapLinkedScripts(linkedScripts, contextPath))
                .replaceFirst(HtmlWriter.ON_FRAME_LOAD_PLACEHOLDER, wrapOnFrameLoadFn(onFrameLoadWriter));

        //only contextualize if there is a valid servlet context path!!
        return TextTools.isEmptyString(contextPath) ? html : contextualize(contextPath, html);
    }

    //TODO this may be an expensive operation, maybe add an option to restrict it to the header?
    private static String contextualize(String contextPath, String html) {
        return html .replaceAll("href=\"/", String.format("href=\"%s/", contextPath))
                    .replaceAll("src=\"/", String.format("src=\"%s/", contextPath));
    }

}
