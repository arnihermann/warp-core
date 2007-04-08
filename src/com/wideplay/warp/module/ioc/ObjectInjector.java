package com.wideplay.warp.module.ioc;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.wideplay.warp.util.reflect.ReflectUtils;
import com.wideplay.warp.util.beans.BeanUtils;
import com.wideplay.warp.module.components.PropertyDescriptor;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: dprasanna
 * Date: 22/03/2007
 * Time: 09:39:53
 * <p/>
 * TODO: Describe me!
 *
 * @author dprasanna
 * @since 1.0
 */
class ObjectInjector {

    public static void injectAll(Collection<PropertyDescriptor> properties, Object target, Object source) {
        for (PropertyDescriptor propertyDescriptor : properties) {
            Object value;

            if (propertyDescriptor.isExpression())
                value =  BeanUtils.getFromPropertyExpression(propertyDescriptor.getValue(), source);
            else
                value = propertyDescriptor.getValue();

            //set the property on the component object
            BeanUtils.setFromPropertyExpression(propertyDescriptor.getName(), target, value);
        }
    }

    //TODO fix generics
    public static Object constructorInject(Class<?> pageClass, Constructor constructor, List<Key<?>> constructorArgs, Injector injector) {
        if (null == constructorArgs) //its a nullary ctor
            return ReflectUtils.instantiate(pageClass);


        //first obtain the ctor parameter instances from guice
        Object[] params = new Object[constructorArgs.size()];   //has to be fast so using an array
        for (int i = 0; i < constructorArgs.size(); i++) {
            Binding binding = injector.getBinding(constructorArgs.get(i));

            //this cannot be validated at start time because bindings may be added dynamically thru the lifetime of the app
            if (null == binding)
                throw new NotInjectableException("There was no binding in guice to inject the constructor parameter type: " + constructorArgs.get(i) + " in class: " + pageClass.getName());

            //everything looks ok, obtain the arg instance
            params[i] = injector.getInstance(constructorArgs.get(i));
        }

        //now invoke the ctor as guice would (but without any other injections)
        return ReflectUtils.instantiate(constructor, params);
    }
}
