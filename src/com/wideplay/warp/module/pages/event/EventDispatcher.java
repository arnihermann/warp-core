package com.wideplay.warp.module.pages.event;

import com.wideplay.warp.util.TextTools;
import com.wideplay.warp.util.reflect.ReflectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: dprasanna
 * Date: 22/03/2007
 * Time: 09:47:01
 * <p/>
 *
 * This is a simple dispatch utility that dispatches events to various handlers in order:
 *
 *  - First events are dispatched to the page object's event handlers
 *  - Next any event delegates that are eligible
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
public class EventDispatcher {

    public static Object resolveAndDispatch(Object bean, String event, Object topic, List<Method> allEventHandlers,
                                            Map<String, Set<Method>> disambiguationEventHandlers,
                                            List<EventHandlerDelegate> eventHandlerDelegateFields) {

        //dont make this static (better memory cadence this way)
        Log log = LogFactory.getLog(EventDispatcher.class);

        //is this a page global event?
         if (TextTools.isEmptyString(event)) {
            Object result = null;

            //yes try to fire it
            for (Method method : allEventHandlers) {
                if (log.isTraceEnabled())
                    log.trace(String.format("Firing 'any' event handler: %s", method.getName()));

                result = invokeEventMethod(method, bean, topic);

                //check for short-circuiting requests
                if (result instanceof Redirection)
                    return result;
            }


            //now do the same for the set of delegates (this is repeated code because otherwise I'd have to do 2 instanceof tests)
            for (EventHandlerDelegate delegate : eventHandlerDelegateFields) {

                //continue to next delegate if this one is filtered out
                if (!delegate.isAnyHandlingSupported())
                    continue;

                for (Method method : delegate.getAllEventHandlers()) {
                    if (log.isTraceEnabled())
                        log.trace("Firing 'any' event handler in delegate: " + method.getName());
                    //read the delegate out of the page object and try to invoke its method
                    result = invokeEventMethod(method, ReflectUtils.readField(delegate.getDelegateFieldDescriptor().getField(), bean), topic);

                    //check for short-circuiting requests
                    if (result instanceof Redirection)
                        return result;
                }
            }

            return result;
        }

        //OTHERWISE:
        if (log.isTraceEnabled())
            log.trace(String.format("Looking for event handler for: %s", event));
        //fire specific handlers
        Object result = null;

        //check page's resolution handlers
        Set<Method> eventMethods = disambiguationEventHandlers.get(event);
        if (null != eventMethods) {
            for (Method method : eventMethods) {
                result = invokeEventMethod(method, bean, topic);

                //check for short-circuiting requests
                if (result instanceof Redirection)
                    return result;
            }
        }



        //now do the same for the set of delegates (this is repeated code because otherwise I'd have to do 2 instanceof tests)
        for (EventHandlerDelegate delegate : eventHandlerDelegateFields) {

            //continue to next delegate if this one is filtered out
            if (!delegate.isSupported(event))
                continue;

            if (log.isTraceEnabled())
                log.trace("Firing event handler thru delegate for: " + event);


            for (Method method : delegate.getDisambiguationEventHandlers().get(event)) {
                //read the delegate out of the page object and try to invoke its method
                result = invokeEventMethod(method, ReflectUtils.readField(delegate.getDelegateFieldDescriptor().getField(), bean), topic);

                //check for short-circuiting requests
                if (result instanceof Redirection)
                    return result;
            }
        }


        return result;
    }

    //invokes the given event method with null or with a topic argument if its parameter size is not zero (TODO should be validated at startup)
    private static Object invokeEventMethod(Method method, Object bean, Object topic) {
        return (0 == method.getParameterTypes().length) ?
                ReflectUtils.invokeMethod(method, bean, null) :
                ReflectUtils.invokeMethod(method, bean, new Object[] { topic } );
    }
}
