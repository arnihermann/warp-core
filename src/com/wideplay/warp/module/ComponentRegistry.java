package com.wideplay.warp.module;

import com.wideplay.warp.module.componentry.ComponentClassReflection;

/**
 * Created with IntelliJ IDEA.
 * On: 8/04/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public interface ComponentRegistry {
    String TEXT_COMPONENT_NAME = "text";
    String VIEWPORT_COMPONENT_NAME = "viewport";

    ComponentClassReflection getRenderableComponent(String elementName);

    Class<?> getTemplateStyleComponent(String componentName);

    void register(String name, Class<?> clazz);

    boolean isRenderableStyle(String componentName);
}
