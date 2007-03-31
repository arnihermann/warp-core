package com.wideplay.warp.module.pages.event;

import com.wideplay.warp.util.reflect.FieldDescriptor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * On: 25/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public interface EventHandlerDelegate {
    List<Method> getAllEventHandlers();

    Map<String, Set<Method>> getDisambiguationEventHandlers();

    FieldDescriptor getDelegateFieldDescriptor();

    boolean isSupported(String eventId);

    boolean isAnyHandlingSupported();
}
