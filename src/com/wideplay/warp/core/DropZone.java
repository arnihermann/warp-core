package com.wideplay.warp.core;

import com.wideplay.warp.annotations.Component;
import com.wideplay.warp.module.components.Renderable;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.HtmlWriter;
import com.google.inject.Injector;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * On: 24/03/2007
 *
 * A simple panel component that can hide or show its contents based on conditions.
 * It is completely transparent to the rendered page (whether contents are shown or not).
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
@Component
public class DropZone implements Renderable {
    private String event;
    private String drop;

    public void render(HtmlWriter writer, List<? extends ComponentHandler> nestedComponents, Injector injector,
                       PageClassReflection reflection, Object page) {

        //draw a span and add drop support to this span
        writer.registerScriptLibrary(CoreScriptLibraries.YUI_DOM_EVENT);
        writer.registerScriptLibrary(CoreScriptLibraries.YUI_DRAGDROP_MIN);

        String id = writer.newId(this);
        writer.element("span", "id", id);

        //write a startup js hook for making this draggable
        writer.writeToOnLoad("new YAHOO.util.DDTarget(\"");
        writer.writeToOnLoad(id);
        writer.writeToOnLoad("\", \"");
        writer.writeToOnLoad(drop); //drop group
        writer.writeToOnLoad("\");");

        //render children
        ComponentSupport.renderMultiple(writer, nestedComponents, injector, reflection, page);

        writer.end("span");
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setDrop(String drop) {
        this.drop = drop;
    }
}
