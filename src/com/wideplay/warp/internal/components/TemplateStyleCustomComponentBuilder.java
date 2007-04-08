package com.wideplay.warp.internal.components;

import com.wideplay.warp.module.ComponentRegistry;
import com.wideplay.warp.rendering.PageHandler;
import com.wideplay.warp.internal.pages.PageBuilders;

import javax.servlet.ServletContext;
import java.util.Map;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * On: 8/04/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
class TemplateStyleCustomComponentBuilder {
    private final ServletContext context;
    private final ComponentRegistry registry;
    private final String packageName;


    public TemplateStyleCustomComponentBuilder(ServletContext context, ComponentRegistry registry, String packageName) {
        this.context = context;
        this.packageName = packageName;
        this.registry = registry;
    }

    void buildAndRegister(String name, Class<?> componentClass) {

        Map<String, PageHandler> customComponentProxy = new HashMap<String, PageHandler>();
        PageBuilders.buildAndStorePageHandler(context, registry, componentClass, packageName, customComponentProxy);


        //extract page handler and convert to viewport-wrapped component

    }
}
