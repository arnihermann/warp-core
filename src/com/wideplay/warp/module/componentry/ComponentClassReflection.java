package com.wideplay.warp.module.componentry;

import com.wideplay.warp.components.AttributesInjectable;
import com.wideplay.warp.module.ioc.el.Expressions;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
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
        return Expressions.evaluate(name, bean);
    }

    public void setPropertyValue(Object bean, String name, Object value) {
        Expressions.write(name, bean, value);
    }

    public Class<? extends Renderable> getComponentClass() {
        return componentClass;
    }

    public boolean isAttributesInjectable() {
        return AttributesInjectable.class.isAssignableFrom(componentClass);
    }

    public Set<String> getWritableProperties() {
        return writableProperties;
    }

    public Set<String> getReadOnlyProperties() {
        return readOnlyProperties;
    }
}
