package com.wideplay.warp.internal.componentry;

import com.wideplay.warp.annotations.Component;
import com.wideplay.warp.module.ComponentRegistry;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.PageHandler;
import com.wideplay.warp.util.TextTools;
import org.dom4j.Document;

import javax.servlet.ServletContext;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * On: Mar 24, 2007 10:34:59 PM
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 */
public class ComponentBuilders {

    private ComponentBuilders() {
    }

    public static ComponentHandler buildComponentHandler(ComponentRegistry componentRegistry, Document document) {
        return new DomComponentHandlerBuilder(componentRegistry).build(document);
    }


    public static ComponentHandler buildComponentHandler(ComponentRegistry componentRegistry, String documentText) {
        return new SimpleTextComponentHandlerBuilder(componentRegistry).build(documentText);
    }



    public static void buildAndRegisterComponent(ServletContext context, ComponentRegistry registry, Class<?> componentClass, String packageName, Map<String, PageHandler> pages) {
        Component componentAnnotation = componentClass.getAnnotation(Component.class);
        String componentName = componentAnnotation.name();

        //default to class's simple name
        if (TextTools.isEmptyString(componentName))
            componentName = componentClass.getSimpleName();

        //register this component by name
        registry.register(componentName, componentClass);
    }

    public static ComponentRegistry newComponentRegistry() {
        return new DefaultComponentRegistryImpl();
    }
}
