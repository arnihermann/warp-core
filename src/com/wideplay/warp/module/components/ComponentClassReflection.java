package com.wideplay.warp.module.components;

import com.wideplay.warp.util.beans.BeanUtils;

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
        return BeanUtils.getFromPropertyExpression(name, bean);
    }

    public void setPropertyValue(Object bean, String name, Object value) {
        BeanUtils.setFromPropertyExpression(name, bean, value);
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
