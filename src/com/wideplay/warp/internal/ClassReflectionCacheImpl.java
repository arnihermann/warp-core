package com.wideplay.warp.internal;

import com.wideplay.warp.module.componentry.ClassReflectionCache;
import com.wideplay.warp.util.reflect.ReflectUtils;
import static com.wideplay.warp.util.reflect.ReflectUtils.getterExists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: dprasanna
 * Date: 26/03/2007
 * Time: 16:36:14
 *
 * This is a simple impl that caches reflection (introspection) results so they can be speeded up.
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
class ClassReflectionCacheImpl implements ClassReflectionCache {
    private final Map<Class<?>, Map<String, String>> classPropertyLabelMap = new ConcurrentHashMap<Class<?>, Map<String, String>>();
    private final Map<Class<?>, Map<String, Class<?>>> classPropertyTypeMap = new ConcurrentHashMap<Class<?>, Map<String, Class<?>>>();

    private final Log log = LogFactory.getLog(ClassReflectionCacheImpl.class);

    public Map<String, String> getPropertyLabelMap(Object object) {
        Class<? extends Object> objectClass = object.getClass();
        Map<String, String> propertyLabels = classPropertyLabelMap.get(objectClass);

        if (log.isDebugEnabled() && null != propertyLabels)
            log.debug(String.format("cache hit! returning class: %s", objectClass));

        //build a property map via reflection
        if (null == propertyLabels) {

            if(log.isDebugEnabled())
                log.debug(String.format("cache miss, introspecting and caching class: %s", objectClass));

            propertyLabels = buildPropertiesAndLabels(objectClass);
            classPropertyLabelMap.put(objectClass, propertyLabels);
        }

        return propertyLabels;
    }


    public Map<String, Class<?>> getPropertyTypeMap(Object object) {
        Class<? extends Object> objectClass = object.getClass();
        Map<String, Class<?>> propertiesAndTypes = classPropertyTypeMap.get(objectClass);

        //build a property/type map via reflection
        if (null == propertiesAndTypes) {
            propertiesAndTypes = buildPropertiesAndTypes(objectClass);
            classPropertyTypeMap.put(objectClass, propertiesAndTypes);
        }

        return propertiesAndTypes;
    }

    private Map<String, Class<?>> buildPropertiesAndTypes(Class<? extends Object> objectClass) {
        Map<String, Class<?>> propertyTypes = new LinkedHashMap<String, Class<?>>();   //MUST preserve order

        //TODO replace with Bean introspector??
        for (Method method : objectClass.getMethods()) {


            //check for getters and cache them as a property
            String name = method.getName();
            if (0 == method.getParameterTypes().length && name.length() > 3 && !void.class.equals(method.getReturnType())
                    && name.startsWith("get")) {

                if ("getClass".equals(method.getName()))
                    continue;

                String key = ReflectUtils.extractPropertyNameFromAccessor(method.getName());

                propertyTypes.put(key, method.getReturnType());

            }
        }

        return propertyTypes;
    }


    //this method is deliberately unsynchronized so 2 threads caching the same class can overwrite the other, thus avoiding competition for the map
    private Map<String, String> buildPropertiesAndLabels(Class<? extends Object> aClass) {
        Map<String, String> propertyLabels = new LinkedHashMap<String, String>();   //MUST preserve order

        ResourceBundle labels;
        try {
            labels = PropertyResourceBundle.getBundle(aClass.getName());
        } catch (MissingResourceException mre) {
            labels = null;
        }

        //TODO replace with Bean introspector??
        final Enumeration<String> keys = labels.getKeys();
        while (keys.hasMoreElements()){
            String prop = keys.nextElement();


            //check for getters and cache them as a property
            if (getterExists(prop, aClass)) {

                //store the binding only if there is a valid getter
                if (null != labels) {
                    //watch for column hides (empty property)
                    String value = labels.getString(prop);
                    if (null != value && !"".equals(value.trim()))
                        propertyLabels.put(prop, value);
                } else
                    //if no binding at all, then use the property name as its label
                    propertyLabels.put(prop, prop);
            }
        }

        return propertyLabels;
    }

}
