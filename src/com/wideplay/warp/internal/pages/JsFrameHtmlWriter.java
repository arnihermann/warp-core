package com.wideplay.warp.internal.pages;

import com.wideplay.warp.rendering.HtmlWriter;
import com.wideplay.warp.rendering.ScriptEvents;
import com.wideplay.warp.rendering.ScriptLibrary;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
class JsFrameHtmlWriter implements HtmlWriter {
    private final StringBuilder writer = new StringBuilder();
    private final StringBuilder onFrameLoadWriter = new StringBuilder();
    private final Set<String> linkedScripts = new LinkedHashSet<String>();

    private static final String PAGE_EVENT_FUNCTION = "fnPublishPageEvent";

    public void registerScriptLibrary(ScriptLibrary library) {
        linkedScripts.add(library.getLibraryURL());
    }

    public void registerEvent(String elementName, ScriptEvents event, String annotation) {
        onFrameLoadWriter.append("document.getElementById(\"");
        onFrameLoadWriter.append(elementName);
        onFrameLoadWriter.append("\").onclick=function(){ __warpForm.w_event.value= \"");
        onFrameLoadWriter.append(annotation);
        onFrameLoadWriter.append("\"; __warpForm.submit(); return false;}; ");
    }

    //write raw text to the body load js func
    public void writeToOnLoad(String text) {
        onFrameLoadWriter.append(text);
    }

    public String newId(Object object) {
        return object.getClass().getName() + "_" + object.hashCode();
    }

    //convenience varargs method
    public void element(String name, Object...nameValuePairs) {
        elementWithAttrs(name, nameValuePairs);
    }


    public void elementWithAttrs(String name, Object[] nameValuePairs) {
        writer.append('<');
        writer.append(name);
        attributes(nameValuePairs);
        writer.append('>');
    }

    public void writeRaw(String text) {
        writer.append(text);
    }

    private void attributes(Object[] nameValuePairs) {
        if (null == nameValuePairs)
            return;

        for (int i = 0; i < nameValuePairs.length; i += 2) {
            writer.append(' ');
            writer.append((String) nameValuePairs[i]);
            writer.append("=\"");
            writer.append(nameValuePairs[i + 1].toString());
            writer.append("\" ");
        }
    }

    public void end(String name) {
        writer.append("</");
        writer.append(name);
        writer.append('>');
    }

    public String getBuffer() {
        //insert the onFrameLoadWriter content in the placeholder
        return writer
                .toString()
                .replaceFirst(HtmlWriter.LINKED_SCRIPTS_PLACEHOLDER, JsSupportUtils.wrapLinkedScripts(linkedScripts))
                .replaceFirst(HtmlWriter.ON_FRAME_LOAD_PLACEHOLDER, JsSupportUtils.wrapOnFrameLoadFn(onFrameLoadWriter));
    }

    public void selfClosedElement(String name, Object... nameValuePairs) {
        selfClosedElementWithAttrs(name, nameValuePairs);
    }

    public void selfClosedElementWithAttrs(String name, Object[] nameValuePairs) {
        writer.append('<');
        writer.append(name);
        attributes(nameValuePairs);
        writer.append("/>");
    }
}
