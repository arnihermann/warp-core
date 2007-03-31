package com.wideplay.warp.internal.components;

import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.module.ComponentRegistry;
import org.dom4j.Document;

/**
 * Created with IntelliJ IDEA.
 * On: Mar 24, 2007 10:34:59 PM
 *
 * @author Dhanji R. Prasanna
 */
public class ComponentBuilders {

    private ComponentBuilders() {
    }

    public static ComponentHandler buildComponentHandler(ComponentRegistry componentRegistry, Document document) {
        return new ComponentHandlerBuilder(componentRegistry).build(document);
    }
}
