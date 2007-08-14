package com.wideplay.warp.components.core;

import com.google.inject.Injector;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.HtmlWriter;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * On: Mar 24, 2007 8:53:18 PM
 *
 * A simple utility that is meant to factor out boilerplate for rendering child components.
 *
 * @author Dhanji R. Prasanna
 */
class ComponentSupport {


    private ComponentSupport() {
    }

    /**
     * A utility method that helps dispatch rendering to a list of ComponentHandlers
     */
    public static void renderMultiple(HtmlWriter writer, List<? extends ComponentHandler> componentHandlers,
                                      Injector injector, PageClassReflection pageReflection, Object page) {
        for (ComponentHandler handler : componentHandlers)
            handler.handleRender(writer, injector, pageReflection, page);
    }

    static String discoverCssAttribute(Object[] attrs) {
        for (int i = 0; i < attrs.length; i++) {
            String attr = (String)attrs[i];

            if ("class".equals(attr)) {
                return (String) attrs[i + 1];
            }
        }

        return null;
    }
}
