package com.wideplay.warp.internal.pages;

import com.wideplay.warp.annotations.OnEvent;
import com.wideplay.warp.util.reflect.ReflectUtils;
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
    private static Log log = LogFactory.getLog(EventHandlerDiscovery.class);

    static Set<Method> discoverDisambiguationHandlers(List<Method> anyEventHandlers, Map<String, Set<Method>> disambiguationEventHandlers) {
        Set<Method> methodsToRemove = new HashSet<Method>();
        for (Method eventHandler : anyEventHandlers) {
            //make methods accessible by reflection if they are not
            if (!eventHandler.isAccessible())
                eventHandler.setAccessible(true);

            log.debug("Processing event handler for disambiguation annotations: " + eventHandler.getName());
            for (Annotation annotation : eventHandler.getDeclaredAnnotations()) {
                //skip OnEvent
                if (OnEvent.class.isAssignableFrom(annotation.getClass()))
                    continue;

                log.debug("Looking at method: " + eventHandler.getName() + " and its annotation: " + annotation.annotationType().getName());


                //if tagged with a custom annotation that is also tagged with OnEvent, we have a disambiguation handler!
                if (annotation.annotationType().isAnnotationPresent(OnEvent.class)) {
                    //first remove handler from the "any" list
                    methodsToRemove.add(eventHandler);

                    String key = ReflectUtils.extractAnnotationSimpleName(annotation.annotationType());
                    Set<Method> handlers = disambiguationEventHandlers.get(key);

                    log.debug("Found event handler for disambiguation annotation: " + key);

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
}
