package com.wideplay.warp.components.core;

import com.google.inject.Injector;
import com.wideplay.warp.annotations.Component;
import com.wideplay.warp.module.components.Renderable;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.HtmlWriter;

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
public class Dialog implements Renderable {
    private boolean visible = true;
    private boolean modal = true;
    private int width;
    private int height;
    private int minWidth;
    private int minHeight;

    public void render(HtmlWriter writer, List<? extends ComponentHandler> nestedComponents, Injector injector, PageClassReflection reflection, Object page) {
            writer.registerScriptLibrary(CoreScriptLibraries.YUI_UTILITIES);
            writer.registerScriptLibrary(CoreScriptLibraries.EXT_YUI_ADAPTER);
            writer.registerScriptLibrary(CoreScriptLibraries.EXT_ALL);

            //draw a div and treat anything inside as contents of the dialog
            String id = writer.newId(this);
            writer.element("div", "id", id);

            //render children
            ComponentSupport.renderMultiple(writer, nestedComponents, injector, reflection, page);

            writer.end("div");

            //configure defaults of dialog
            if (0 == minWidth)
                minWidth = width;
            if (0 == minHeight)
                minHeight = height;

            //write a startup js hook for making the div a dialog
            writer.writeToOnLoad("new Ext.BasicDialog(\"");
            writer.writeToOnLoad(id);
            writer.writeToOnLoad("\", {");
            writer.writeToOnLoad("modal:");
            writer.writeToOnLoad(Boolean.toString(modal)); //drag group

            writer.writeToOnLoad(",width:");
            writer.writeToOnLoad(Integer.toString(width));

            writer.writeToOnLoad(",height:");
            writer.writeToOnLoad(Integer.toString(width));

            writer.writeToOnLoad(",shadow:true");

            writer.writeToOnLoad(",minWidth:");
            writer.writeToOnLoad(Integer.toString(minWidth));
            writer.writeToOnLoad(",minHeight:");
            writer.writeToOnLoad(Integer.toString(minHeight));

            writer.writeToOnLoad("}).show();");
    }


    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }


    public void setHeight(int height) {
        this.height = height;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    public void setModal(boolean modal) {
        this.modal = modal;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
