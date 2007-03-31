package com.wideplay.warp.module.pages.event;

import com.wideplay.warp.util.reflect.ReflectUtils;
import com.wideplay.warp.util.TextTools;
import com.wideplay.warp.module.pages.PageClassReflection;
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
 * TODO: Describe me!
 *
 * @author dprasanna
 * @since 1.0
 */
public class EventDispatcher {
    private static Log log = LogFactory.getLog(EventDispatcher.class);

    public static Object resolveAndDispatchWithDelegates(Object bean, String event, List<Method> allEventHandlers,
                                            Map<String, Set<Method>> disambiguationEventHandlers,
                                            List<EventHandlerDelegate> eventHandlerDelegateFields) {


        //is this a page global event?
         if (TextTools.isEmptyString(event)) {
            Object result = null;

            //yes try to fire it
            for (Method method : allEventHandlers) {
                log.trace("Firing 'any' event handler: " + method.getName());
                result = ReflectUtils.invokeMethod(method, bean, null);

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
                    log.trace("Firing 'any' event handler in delegate: " + method.getName());
                    //read the delegate out of the page object and try to invoke its method
                    result = ReflectUtils.invokeMethod(method, ReflectUtils.readField(delegate.getDelegateFieldDescriptor().getField(), bean), null);

                    //check for short-circuiting requests
                    if (result instanceof Redirection)
                        return result;
                }
            }

            return result;
        }

        //OTHERWISE:

        log.trace("Firing event handler for: " + event);
        //fire specific handlers
        Object result = null;

        //check page's resolution handlers
        Set<Method> eventMethods = disambiguationEventHandlers.get(event);
        if (null != eventMethods) {
            for (Method method : eventMethods) {
                result = ReflectUtils.invokeMethod(method, bean, null);

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

            log.trace("Firing event handler thru delegate for: " + event);


            for (Method method : delegate.getDisambiguationEventHandlers().get(event)) {
                //read the delegate out of the page object and try to invoke its method
                result = ReflectUtils.invokeMethod(method, ReflectUtils.readField(delegate.getDelegateFieldDescriptor().getField(), bean), null);

                //check for short-circuiting requests
                if (result instanceof Redirection)
                    return result;
            }
        }


        return result;
    }
}
