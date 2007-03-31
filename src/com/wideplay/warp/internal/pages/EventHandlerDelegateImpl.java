package com.wideplay.warp.internal.pages;

import com.wideplay.warp.util.reflect.FieldDescriptor;
import com.wideplay.warp.util.reflect.ReflectUtils;
import com.wideplay.warp.module.pages.event.EventHandlerDelegate;

import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.LinkedHashSet;

/**
 * Created with IntelliJ IDEA.
* On: 25/03/2007
*
* @author Dhanji R. Prasanna
* @since 1.0
*/
class EventHandlerDelegateImpl implements EventHandlerDelegate {
    private final FieldDescriptor delegateFieldDescriptor;
    private final List<Method> allEventHandlers;
    private final Map<String, Set<Method>> disambiguationEventHandlers;
    private final Set<Class<? extends Annotation>> resolutionAnnotations;
    private final Set<String> resolutionAnnotationsStrings;

    private EventHandlerDelegateImpl(FieldDescriptor delegateFieldDescriptor, List<Method> allEventHandlers,
                                Map<String, Set<Method>> disambiguationEventHandlers,
                                Set<Class<? extends Annotation>> resolutionAnnotations) {

        this.delegateFieldDescriptor = delegateFieldDescriptor;
        this.allEventHandlers = allEventHandlers;
        this.disambiguationEventHandlers = disambiguationEventHandlers;
        this.resolutionAnnotations = resolutionAnnotations;

        //convert resolution annotations to strings
        resolutionAnnotationsStrings = new LinkedHashSet<String>();
        for (Class<? extends Annotation> resolutionAnnotation : resolutionAnnotations)
            resolutionAnnotationsStrings.add(ReflectUtils.extractAnnotationSimpleName(resolutionAnnotation));
    }


    public List<Method> getAllEventHandlers() {
        return allEventHandlers;
    }

    public Map<String, Set<Method>> getDisambiguationEventHandlers() {
        return disambiguationEventHandlers;
    }


    public FieldDescriptor getDelegateFieldDescriptor() {
        return delegateFieldDescriptor;
    }

    public boolean isSupported(String eventId) {
        return resolutionAnnotationsStrings.contains(eventId);
    }

    public boolean isAnyHandlingSupported() {
        return resolutionAnnotations.isEmpty();
    }

    public static EventHandlerDelegateImpl newDelegate(FieldDescriptor delegateFieldDescriptor, List<Method> allEventHandlers,
                                Map<String, Set<Method>> disambiguationEventHandlers, Set<Class<? extends Annotation>> resolutionAnnotations) {

        return new EventHandlerDelegateImpl(delegateFieldDescriptor, allEventHandlers, disambiguationEventHandlers, resolutionAnnotations);
    }
}
