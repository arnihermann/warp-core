package com.wideplay.warp.internal.pages;

import com.google.inject.Inject;
import com.google.inject.Key;
import com.wideplay.warp.annotations.Managed;
import com.wideplay.warp.annotations.OnEvent;
import com.wideplay.warp.annotations.OnEvents;
import com.wideplay.warp.module.WarpConfigurationException;
import com.wideplay.warp.module.pages.event.EventHandlerDelegate;
import com.wideplay.warp.util.reflect.FieldDescriptor;
import com.wideplay.warp.util.reflect.ReflectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;


/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
class PageClassReflectionBuilder {
    private final Class<?> pageClass;

    private static final Set<String> reservedMethods = new HashSet<String>();

    static {
        reservedMethods.add("getClass");
    }

    public PageClassReflectionBuilder(Class<?> pageClass) {
        this.pageClass = pageClass;
    }

    private final Log log = LogFactory.getLog(getClass());

    public PageClassReflectionImpl build() {
        //validate class
        if (pageClass.isEnum() || pageClass.isAnnotation())
            throw new WarpConfigurationException("Page class cannot be an enum or an annotation type: " + pageClass.getName());


        log.debug(String.format("Attempting to build class reflection for page class: %s", pageClass.getName()));
        final Map<String, Method> setters = new LinkedHashMap<String, Method>();
        final Map<String, Method> getters = new LinkedHashMap<String, Method>();
        discoverGettersAndSetters(setters, getters);


        //add any method annotated with @OnEvent to event handlers
        List<Method> anyEventHandlers = ReflectUtils.findDeclaredMethodsWithAnnotationAsList(pageClass, OnEvent.class);
        ReflectUtils.makeMethodsAccessible(anyEventHandlers);
        Map<String, Set<Method>> disambiguationEventHandlers = new LinkedHashMap<String, Set<Method>>();


        
        //discover those methods that have custom event disambiguation annotations
        Set<Method> methodsToRemove = EventHandlerDiscovery.discoverDisambiguationHandlers(anyEventHandlers, disambiguationEventHandlers);

        //remove methods separately to prevent breaking the iterator
        anyEventHandlers.removeAll(methodsToRemove);



        
        //discover event handler delegates and their disambiguation handlers (DELEGATES)
        Set<FieldDescriptor> delegateEventHandlerFields = ReflectUtils.findDeclaredFieldDescriptorsWithAnnotation(pageClass, OnEvents.class);
        List<EventHandlerDelegate> delegates = new ArrayList<EventHandlerDelegate>(delegateEventHandlerFields.size());

        ReflectUtils.makeFieldDescriptorsAccessible(delegateEventHandlerFields);
        //iterate field types and discover handlers from them
        for (FieldDescriptor fieldDescriptor : delegateEventHandlerFields) {
            Class<?> delegateClass = fieldDescriptor.getFieldType();
            List<Method> delegateAnyEventHandlers = ReflectUtils.findDeclaredMethodsWithAnnotationAsList(delegateClass, OnEvent.class);
            ReflectUtils.makeMethodsAccessible(anyEventHandlers);

            Map<String, Set<Method>> delegateDisambiguationEventHandlers = new LinkedHashMap<String, Set<Method>>();

            //discover those methods that have custom event disambiguation annotations
            Set<Method> methodsToRemoveFromDelegate = EventHandlerDiscovery.discoverDisambiguationHandlers(delegateAnyEventHandlers, delegateDisambiguationEventHandlers);
            //remove methods separately to prevent breaking the iterator
            delegateAnyEventHandlers.removeAll(methodsToRemoveFromDelegate);

            //discover the filter annotations (the @OnEvents(....) )
            Set<Class<? extends Annotation>> eventFilterAnnotations = new HashSet<Class<? extends Annotation>>();
            OnEvents delegateAnnotation = fieldDescriptor.getField().getAnnotation(OnEvents.class);
            if (null != delegateAnnotation)
                Collections.addAll(eventFilterAnnotations, delegateAnnotation.value());

            //store to the set of delegates
            delegates.add(EventHandlerDelegateImpl.newDelegate(fieldDescriptor, delegateAnyEventHandlers,
                    delegateDisambiguationEventHandlers, eventFilterAnnotations));
        }



        log.debug(String.format("Registered 'any' handlers: %s", anyEventHandlers));


        //discover managed property fields and store them
        Set<FieldDescriptor> managedFields = ReflectUtils.findDeclaredFieldDescriptorsWithAnnotation(pageClass, Managed.class);
        //make them accessible
        for (FieldDescriptor fieldDescriptor : managedFields)
            if (!fieldDescriptor.getField().isAccessible())
                fieldDescriptor.getField().setAccessible(true);




        //discover and build injectable constructor
        Constructor injectable = ReflectUtils.findDeclaredConstructorWithAnnotation(pageClass, Inject.class);
        List<Key<?>> argKeys = null;



        //find constructor's parameter anntotations and store keys for them
        if (null != injectable) {
            Class<?>[] args = injectable.getParameterTypes();
            argKeys = GuiceAnnotationsDiscovery.discoverParameterAnnotations(args, argKeys, injectable);
        } else
            //if none, try to find a nullary constructor
            injectable = ReflectUtils.findNullaryConstructor(pageClass);

        //make the ctor accessible just in case its private or somethin
        ReflectUtils.makeMemberAccessible(injectable);

        
        
        //no viable ctor!! error!!
        if (null == injectable)
            throw new WarpConfigurationException("Page class has got neither guice-injectable (@Inject) constructors nor a nullary (default) constructor; there is no strategy to create instances: " + pageClass.getName());



        return new PageClassReflectionImpl(pageClass, getters, setters, anyEventHandlers, disambiguationEventHandlers,
                managedFields, injectable, argKeys, delegates);
    }


    




    private void discoverGettersAndSetters(Map<String, Method> setters, Map<String, Method> getters) {
        //add any method starting with "set" and taking one parameter and returning void
        for (Method method : pageClass.getMethods())
            if (method.getName().startsWith("set") && method.getParameterTypes().length == 1 && void.class.equals(method.getReturnType())) {
                log.debug("setter discovered: " + method.getName());
                setters.put(ReflectUtils.extractPropertyNameFromAccessor(method.getName()), method);
            }

        //add any method starting with "getValue" and taking no parameter and not returning void
        for (Method method : pageClass.getMethods())
            if (method.getName().startsWith("get") && method.getParameterTypes().length == 0 && !void.class.equals(method.getReturnType())) {
                //skip special methods
                if (reservedMethods.contains(method.getName()))
                    continue;

                log.debug("getter discovered: " + method.getName());
                getters.put(ReflectUtils.extractPropertyNameFromAccessor(method.getName()), method);
            }
    }
}
