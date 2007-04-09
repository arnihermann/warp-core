package com.wideplay.warp.internal.components;

import com.wideplay.warp.module.ComponentRegistry;
import com.wideplay.warp.module.components.Renderable;
import com.wideplay.warp.module.components.ComponentClassReflection;
import com.wideplay.warp.core.*;

import java.util.Map;
import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * A registry for obtaining available components that support a template.
 *
 * User components should be registered here at module load time.
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
class DefaultComponentRegistryImpl implements ComponentRegistry {
    private final Map<String, Class<? extends Renderable>> renderableComponentsByName = new LinkedHashMap<String, Class<? extends Renderable>>();
    private final Map<Class<? extends Renderable>, ComponentClassReflection> componentClassReflections = new LinkedHashMap<Class<? extends Renderable>, ComponentClassReflection>();
    private final Map<String, Class<?>> templateStyleComponentsByName = new LinkedHashMap<String, Class<?>>();

    public DefaultComponentRegistryImpl() {
        //register default components
        register("frame", Frame.class);
        register("link", Link.class);
        register(ComponentRegistry.TEXT_COMPONENT_NAME, RawText.class);
        register("button", Button.class);
        register("panel", Panel.class);
        register("textfield", TextField.class);
        register("selectbox", SelectBox.class);
        register("table", Table.class);
        register("closure", Closure.class);
        register("dropzone", DropZone.class);
        register("checkbox", Checkbox.class);
        register(ComponentRegistry.VIEWPORT_COMPONENT_NAME, Viewport.class);
    }

    public ComponentClassReflection getRenderableComponent(String name) {
        //neutralize case
        name = name.toLowerCase();

        Class<? extends Renderable> componentClass = renderableComponentsByName.get(name);

        verifyNotNull(componentClass, name);

        return componentClassReflections.get(componentClass);
    }

    public Class<?> getTemplateStyleComponent(String name) {
        //neutralize case
        name = name.toLowerCase();

        Class<?> componentClass = templateStyleComponentsByName.get(name);

        verifyNotNull(componentClass, name);

        return componentClass;
    }

    private void verifyNotNull(Class<?> componentClass, String elementName) {
        if (null == componentClass)
            throw new NoSuchComponentException("No component was registered for the component key: " + elementName);
    }

    public void register(String name, Class<?> clazz) {
        //neutralize case
        name = name.toLowerCase();

        //do some checking
        if (renderableComponentsByName.containsKey(name) || templateStyleComponentsByName.containsKey(name))
            throw new DuplicateComponentException("Component name " + name + " is already registered to class: " + renderableComponentsByName.get(name).getName());

        //register to appropriate set
        if (Renderable.class.isAssignableFrom(clazz)) {
            Class<? extends Renderable> renderableClass = (Class<? extends Renderable>) clazz;
            renderableComponentsByName.put(name, renderableClass);
            
            //also store a reflection
            componentClassReflections.put(renderableClass, new ComponentClassReflectionBuilder(renderableClass).build());
        }
        else
            templateStyleComponentsByName.put(name, clazz);
    }

    public boolean isRenderableStyle(String componentName) {
        return renderableComponentsByName.containsKey(componentName);
    }
}
