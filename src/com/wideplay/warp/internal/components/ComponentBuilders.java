package com.wideplay.warp.internal.components;

import com.wideplay.warp.annotations.Component;
import com.wideplay.warp.internal.pages.PageBuilders;
import com.wideplay.warp.module.ComponentRegistry;
import com.wideplay.warp.module.components.Renderable;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.PageHandler;
import com.wideplay.warp.util.TextTools;
import org.dom4j.Document;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

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

    public static void buildAndRegisterComponent(ServletContext context, ComponentRegistry registry, String packageName, Class<?> componentClass) {
        Component componentAnnotation = componentClass.getAnnotation(Component.class);
        String componentName = componentAnnotation.name();

        //default to class's simple name
        if (TextTools.isEmptyString(componentName))
            componentName = componentClass.getSimpleName();

        //is this a renderable-style component?
        if(Renderable.class.isAssignableFrom(componentClass)) {
            registry.register(componentName, (Class<? extends Renderable>) componentClass);
            return;
        }

        //otherwise this is a template-style component
        new TemplateStyleCustomComponentBuilder(context, registry, packageName).buildAndRegister(componentName, componentClass);
    }
}
