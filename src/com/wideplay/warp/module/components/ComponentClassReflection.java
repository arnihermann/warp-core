package com.wideplay.warp.module.components;

import com.wideplay.warp.util.reflect.ReflectUtils;
import com.wideplay.warp.module.components.Renderable;
import com.wideplay.warp.module.NotWritablePropertyException;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class ComponentClassReflection {
    private final Class<? extends Renderable> componentClass;
    private final Map<String, Method> setters;
    private final Map<String, Method> getters;

    private final Set<String> writableProperties;
    private final Set<String> readOnlyProperties;

    public ComponentClassReflection(Class<? extends Renderable> componentClass, Map<String, Method> getters, Map<String, Method> setters) {
        this.componentClass = componentClass;
        this.getters = getters;
        this.setters = setters;

        writableProperties = setters.keySet();
        readOnlyProperties = getters.keySet();
        readOnlyProperties.removeAll(writableProperties);
    }

    public Object getPropertyValue(Object bean, String name) {
        return ReflectUtils.invokeMethod(getters.get(name), bean, null);
    }

    public void setPropertyValue(Object bean, String name, Object value) {
        Method setter = setters.get(name);
        if (null == setter)
            throw new NotWritablePropertyException("No setter was found for property: " + name);

        ReflectUtils.invokeMethod(setter, bean, new Object[] { value });
    }

    public Class<? extends Renderable> getComponentClass() {
        return componentClass;
    }

    public Set<String> getWritableProperties() {
        return writableProperties;
    }

    public Set<String> getReadOnlyProperties() {
        return readOnlyProperties;
    }
}
