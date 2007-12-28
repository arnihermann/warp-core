package com.wideplay.warp.internal.pages;

import com.wideplay.warp.components.AttributesInjectable;
import com.wideplay.warp.rendering.*;
import com.wideplay.warp.util.TextTools;
import com.wideplay.warp.annotations.Context;
import com.google.inject.Inject;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
class JsFrameHtmlWriter implements HtmlWriter {
    private final Set<String> linkedScripts = new LinkedHashSet<String>();
    private final SimpleHtmlWriter simpleHtmlWriter = new SimpleHtmlWriter();
    private final StringBuilder onFrameLoadWriter = new StringBuilder();
    
    private final String contextPath;

    @Inject
    public JsFrameHtmlWriter(@Context HttpServletRequest request) {
        this.contextPath = request.getContextPath();
    }

    //TODO this may be an expensive operation, maybe add an option to restrict it to the header?
    private static String contextualize(String contextPath, String html) {
        return html .replaceAll("href=\"/", String.format("href=\"%s/", contextPath))
                    .replaceAll("src=\"/", String.format("src=\"%s/", contextPath));
    }

    public void registerScriptLibrary(ScriptLibrary library) {
        linkedScripts.add(library.getLibraryURL());
    }

    public String newId(Object object) {
        return simpleHtmlWriter.newId(object);
    }

    public String makeIdFor(AttributesInjectable object) {
        return simpleHtmlWriter.makeIdFor(object);
    }

    public void element(String name, Object... nameValuePairs) {
        simpleHtmlWriter.element(name, nameValuePairs);
    }

    public void elementWithAttrs(String name, Object[] nameValuePairs) {
        simpleHtmlWriter.elementWithAttrs(name, nameValuePairs);
    }

    public void writeRaw(String text) {
        simpleHtmlWriter.writeRaw(text);
    }

    public void end(String name) {
        simpleHtmlWriter.end(name);
    }

    public void selfClosedElement(String name, Object... nameValuePairs) {
        simpleHtmlWriter.selfClosedElement(name, nameValuePairs);
    }

    public void selfClosedElementWithAttrs(String name, Object[] nameValuePairs) {
        simpleHtmlWriter.selfClosedElementWithAttrs(name, nameValuePairs);
    }

    public StringBuilder getWriter() {
        return simpleHtmlWriter.getWriter();
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

    //write raw text to the body load js func
    public void writeToOnLoad(String text) {
        onFrameLoadWriter.append(text);
    }

    public void registerInputBinding(String id) {
        //do nothing...
    }

    public String getBuffer() {
        //insert the onFrameLoadWriter content in the placeholder
        final String html = simpleHtmlWriter.getWriter()
                .toString()
                .replaceFirst(HtmlWriter.LINKED_SCRIPTS_PLACEHOLDER, com.wideplay.warp.internal.pages.JsSupportUtils.wrapLinkedScripts(linkedScripts, contextPath))
                .replaceFirst(HtmlWriter.ON_FRAME_LOAD_PLACEHOLDER, com.wideplay.warp.internal.pages.JsSupportUtils.wrapOnFrameLoadFn(onFrameLoadWriter));

        //only contextualize if there is a valid servlet context path!!
        return TextTools.isEmptyString(contextPath) ? html : JsFrameHtmlWriter.contextualize(contextPath, html);
    }

}
