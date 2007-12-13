package com.wideplay.warp.components.core;

import com.wideplay.warp.annotations.Component;
import com.wideplay.warp.module.componentry.Renderable;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.RenderingContext;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: dprasanna
 * Date: 12/06/2007
 * Time: 11:57:12
 * <p/>
 *
 * A sub-component of Table. To be used to override or add to the default behavior
 *  of a table component's column-rendering.
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
@Component
public class Column implements Renderable {
    private String property;
    private String title;
    static final String PROPERTY = "property";
    static final String TITLE = "title";

    public void render(RenderingContext context, List<? extends ComponentHandler> nestedComponents) {
        ComponentSupport.renderMultiple(context, nestedComponents);
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
