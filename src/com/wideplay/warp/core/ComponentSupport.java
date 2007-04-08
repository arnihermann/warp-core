package com.wideplay.warp.core;

import com.google.inject.Injector;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.HtmlWriter;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * On: Mar 24, 2007 8:53:18 PM
 *
 * @author Dhanji R. Prasanna
 */
public class ComponentSupport {


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
}
