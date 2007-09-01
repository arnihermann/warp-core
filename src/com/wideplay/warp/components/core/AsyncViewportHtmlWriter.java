package com.wideplay.warp.components.core;

import com.wideplay.warp.components.AttributesInjectable;
import com.wideplay.warp.rendering.HtmlWriter;
import com.wideplay.warp.rendering.ScriptEvents;
import com.wideplay.warp.rendering.ScriptLibrary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * On: 28/08/2007
 *
 * A special proxy pattern that intercepts calls by input components so it
 * can build a list of input bindings to "watch"
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
class AsyncViewportHtmlWriter implements HtmlWriter {
    private final HtmlWriter delegate;
    private final List<String> bindings = new ArrayList<String>();
    private final String viewportId;

    public AsyncViewportHtmlWriter(String id, HtmlWriter delegate) {
        this.delegate = delegate;
        this.viewportId = id;
    }


    public void registerInputBinding(String id) {
        bindings.add(id);
    }

    public List<String> getBindings() {
        return bindings;
    }


    //rest are simply delegating wrappers
    public void element(String name, Object... nameValuePairs) {
        delegate.element(name, nameValuePairs);
    }

    public void elementWithAttrs(String name, Object[]... nameValuePairs) {
        delegate.elementWithAttrs(name, nameValuePairs);
    }

    public void end(String name) {
        delegate.end(name);
    }

    public String getBuffer() {
        return delegate.getBuffer();
    }

    public String newId(Object object) {
        return delegate.newId(object);
    }

    public String makeIdFor(AttributesInjectable object) {
        return String.format("%s_%s", viewportId, delegate.makeIdFor(object));
    }

    public void registerEvent(String elementName, ScriptEvents event, String annotation, int topicId) {
        delegate.registerEvent(elementName, event, annotation, topicId);
    }

    public void registerScriptLibrary(ScriptLibrary library) {
        delegate.registerScriptLibrary(library);
    }

    public void registerAsyncEvent(String id, ScriptEvents click, String encodedEvent, int topicId, String[] viewports) {
        delegate.registerAsyncEvent(id, click, encodedEvent, topicId, viewports);
    }

    public void selfClosedElement(String name, Object... nameValuePairs) {
        delegate.selfClosedElement(name, nameValuePairs);
    }

    public void selfClosedElementWithAttrs(String name, Object[] nameValuePairs) {
        delegate.selfClosedElementWithAttrs(name, nameValuePairs);
    }

    public void writeRaw(String text) {
        delegate.writeRaw(text);
    }

    public void writeToOnLoad(String text) {
        delegate.writeToOnLoad(text);
    }
}
