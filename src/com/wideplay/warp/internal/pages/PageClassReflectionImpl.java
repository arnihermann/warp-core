package com.wideplay.warp.internal.pages;

import com.google.inject.Key;
import com.wideplay.warp.module.ioc.el.Expressions;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.module.pages.event.EventDispatcher;
import com.wideplay.warp.module.pages.event.EventHandlerDelegate;
import com.wideplay.warp.util.reflect.FieldDescriptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * TODO fix generics
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
class PageClassReflectionImpl implements PageClassReflection {
    private final Class<?> pageClass;

    private final Map<String, Method> setters;
    private final Map<String, Method> getters;

    private final List<Method> allEventHandlers;
    private final Map<String, Set<Method>> disambiguationEventHandlers;
    private final List<EventHandlerDelegate> eventHandlerDelegateFields;

    private final Set<FieldDescriptor> managedFields;

    private final Constructor constructor;
    private final List<Key<?>> constructorArgs;

    private final Log log = LogFactory.getLog(getClass());

    public PageClassReflectionImpl(Class<?> pageClass, Map<String, Method> getters, Map<String, Method> setters,
                               List<Method> allEventHandlers, Map<String, Set<Method>> disambiguationEventHandlers,
                               Set<FieldDescriptor> managedFields, Constructor constructor, List<Key<?>> constructorArgs,
                               List<EventHandlerDelegate> eventHandlerDelegateFields) {

        this.pageClass = pageClass;
        this.getters = getters;
        this.setters = setters;
        this.allEventHandlers = allEventHandlers;
        this.disambiguationEventHandlers = disambiguationEventHandlers;
        this.managedFields = managedFields;
        this.constructor = constructor;
        this.constructorArgs = constructorArgs;
        this.eventHandlerDelegateFields = eventHandlerDelegateFields;
    }

    public Object getPropertyValue(Object bean, String name) {
        return Expressions.evaluate(name, bean);
    }


    public void setPropertyValue(Object bean, String name, Object value) {
        Expressions.write(name, bean, value);
    }

    public Class<?> getPageClass() {
        return pageClass;
    }

    //fires all event handlers (default action)
    public Object fireEvent(Object bean, String event, Object topic) {
        return EventDispatcher.resolveAndDispatch(bean, event, topic, allEventHandlers, disambiguationEventHandlers, eventHandlerDelegateFields);
    }

    public Set<FieldDescriptor> getManagedFields() {
        return managedFields;
    }


    public Map<String, Method> getGetters() {
        return getters;
    }

    public Map<String, Method> getSetters() {
        return setters;
    }
}
