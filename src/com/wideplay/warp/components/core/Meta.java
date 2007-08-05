package com.wideplay.warp.components.core;

import com.google.inject.Injector;
import com.wideplay.warp.module.componentry.Renderable;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.HtmlWriter;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * On: 5/08/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class Meta implements Renderable {

    public void render(HtmlWriter writer, List<? extends ComponentHandler> nestedComponents, Injector injector,
                       PageClassReflection reflection, Object page) {
        writer.element("head");

        //write user's stuff:
        ComponentSupport.renderMultiple(writer, nestedComponents, injector, reflection, page);

        //write some script framing
        writer.writeRaw(HtmlWriter.LINKED_SCRIPTS_PLACEHOLDER);

        //register common script libraries needed by all components

        //write a placeholder for the onFrameLoad function
        writer.element("script", "type", "text/javascript");
        writer.writeRaw(HtmlWriter.ON_FRAME_LOAD_PLACEHOLDER);
        writer.end("script");
        
        writer.end("head");

    }
}
