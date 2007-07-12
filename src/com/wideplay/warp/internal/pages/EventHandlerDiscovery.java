package com.wideplay.warp.internal.pages;

import com.wideplay.warp.annotations.OnEvent;
import com.wideplay.warp.util.reflect.ReflectUtils;
import com.wideplay.warp.module.WarpConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * On: 22/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
class EventHandlerDiscovery {

    static Set<Method> discoverDisambiguationHandlers(List<Method> anyEventHandlers, Map<String, Set<Method>> disambiguationEventHandlers) {
        Log log = LogFactory.getLog(EventHandlerDiscovery.class);


        Set<Method> methodsToRemove = new HashSet<Method>();
        for (Method eventHandler : anyEventHandlers) {
            //make methods accessible by reflection if they are not
            if (!eventHandler.isAccessible())
                eventHandler.setAccessible(true);

            //validate event method
            validate(eventHandler);

            for (Annotation annotation : eventHandler.getDeclaredAnnotations()) {
                //skip OnEvent
                if (OnEvent.class.isAssignableFrom(annotation.getClass()))
                    continue;

                if (log.isDebugEnabled())
                    log.debug(String.format("Looking at method '%s' and its annotation: %s", eventHandler.getName(), annotation.annotationType().getName()));


                //if tagged with a custom annotation that is also tagged with OnEvent, we have a disambiguation handler!
                if (annotation.annotationType().isAnnotationPresent(OnEvent.class)) {
                    //first remove handler from the "any" list
                    methodsToRemove.add(eventHandler);

                    String key = ReflectUtils.extractAnnotationSimpleName(annotation.annotationType());
                    Set<Method> handlers = disambiguationEventHandlers.get(key);

                    if (log.isDebugEnabled())
                        log.debug(String.format("Found event handler for disambiguation annotation: %s", key));

                    //create a new set if there isnt one
                    if (null == handlers) {
                        handlers = new LinkedHashSet<Method>();
                        disambiguationEventHandlers.put(key, handlers);
                    }

                    //add the handler
                    handlers.add(eventHandler);
                }
            }
        }
        return methodsToRemove;
    }

    private static void validate(Method eventHandler) {
        if (eventHandler.getParameterTypes().length > 1)
            throw new WarpConfigurationException("Event handler methods can take at most one argument (topic): " + eventHandler.getName());
    }
}
