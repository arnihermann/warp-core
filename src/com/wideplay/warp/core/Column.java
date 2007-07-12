package com.wideplay.warp.core;

import com.wideplay.warp.module.components.Renderable;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.annotations.Component;
import com.wideplay.warp.rendering.HtmlWriter;
import com.wideplay.warp.rendering.ComponentHandler;
import com.google.inject.Injector;

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
 * @author dprasanna
 * @since 1.0
 */
@Component
public class Column implements Renderable {
    private String property;
    private String title;
    static final String PROPERTY = "property";
    static final String TITLE = "title";

    public void render(HtmlWriter writer, List<? extends ComponentHandler> nestedComponents, Injector injector, PageClassReflection reflection, Object page) {
        ComponentSupport.renderMultiple(writer, nestedComponents, injector, reflection, page);
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
