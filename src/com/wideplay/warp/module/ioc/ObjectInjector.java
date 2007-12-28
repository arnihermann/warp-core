package com.wideplay.warp.module.ioc;

import com.wideplay.warp.module.componentry.PropertyDescriptor;
import com.wideplay.warp.module.ioc.el.Expressions;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: dprasanna
 * Date: 22/03/2007
 * Time: 09:39:53
 * <p/>
 *
 * A static utility that factors out boilerplate for injecting objects (in cases where guice is insufficient--hopefully guice1.1
 * and ctor interception will obviate the need for this).
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
class ObjectInjector {

    public static void injectAll(Collection<PropertyDescriptor> properties, Object target, Object source) {
        for (PropertyDescriptor propertyDescriptor : properties) {
            Object value;

            if (propertyDescriptor.isExpression())
                value =  Expressions.evaluate(propertyDescriptor.getValue(), source);
            else
                value = propertyDescriptor.getValue();

            //set the property on the component object
            Expressions.write(propertyDescriptor.getName(), target, value);
        }
    }

}
