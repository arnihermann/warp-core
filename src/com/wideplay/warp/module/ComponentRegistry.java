package com.wideplay.warp.module;

import com.wideplay.warp.module.components.Renderable;
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
public class ComponentRegistry {
    private final Map<String, Class<? extends Renderable>> componentsByName = new LinkedHashMap<String, Class<? extends Renderable>>();
    public static final String TEXT_COMPONENT_NAME = "text";

    public ComponentRegistry() {
        //register default components TODO move this out to proper module reg?
        register("frame", Frame.class);
        register("link", Link.class);
        register("text", RawText.class);
        register("button", Button.class);
        register("panel", Panel.class);
        register("textfield", TextField.class);
        register("selectbox", SelectBox.class);
        register("table", Table.class);
        register("closure", Closure.class);
        register("dropzone", DropZone.class);
        register("viewport", Viewport.class);
    }

    public synchronized Class<? extends Renderable> getComponent(String elementName) {
        Class<? extends Renderable> componentClass = componentsByName.get(elementName);

        if (null == componentClass)
            throw new NoSuchComponentException("no component was registered for the component key: " + elementName);

        return componentClass;
    }

    public synchronized void register(String name, Class<? extends Renderable> clazz) {
        componentsByName.put(name, clazz);
    }
}
