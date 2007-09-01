package com.wideplay.warp.components.core;

import com.wideplay.warp.annotations.Component;
import com.wideplay.warp.module.componentry.Renderable;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.RenderingContext;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * On: 24/03/2007
 *
 * A simple panel component that can hide or show its contents based on conditions.
 * It is completely transparent to the rendered page (whether contents are shown or not).
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
@Component
public class Panel implements Renderable {
    boolean visible = true;

    public void render(RenderingContext context, List<? extends ComponentHandler> nestedComponents) {
        if (visible) {
            //no tag is drawn around a panel (unless for dragging support?)
            ComponentSupport.renderMultiple(context, nestedComponents);
        }
    }


    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
