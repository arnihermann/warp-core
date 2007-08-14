package com.wideplay.warp.components.core;

import com.google.inject.Injector;
import com.wideplay.warp.annotations.Component;
import com.wideplay.warp.module.componentry.Renderable;
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
public class Panel implements Renderable {
    boolean visible = true;

    public void render(HtmlWriter writer, List<? extends ComponentHandler> nestedComponents, Injector injector, PageClassReflection reflection, Object page) {
        if (visible) {

            //draw a div and add drag support to this panel if draggable only
            ComponentSupport.renderMultiple(writer, nestedComponents, injector, reflection, page);
        }
    }


    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
