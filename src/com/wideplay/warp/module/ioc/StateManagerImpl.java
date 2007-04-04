package com.wideplay.warp.module.ioc;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Inject;
import com.wideplay.warp.module.StateManager;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.util.Cube;
import com.wideplay.warp.util.HashCube;
import com.wideplay.warp.util.reflect.FieldDescriptor;
import com.wideplay.warp.util.reflect.ReflectUtils;

import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * On: 20/03/2007
 *
 * This should never be a singleton, only a session-scoped object or less.
 * 
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
class StateManagerImpl implements StateManager {
    @Inject private Injector injector;

    //TODO add additional cubes at different scopes?
    //TODO fix generics
    public synchronized void injectManaged(PageClassReflection reflection, Object page) {
        //lookup scoped cube
        Cube<Class<?>, String, Object> pagesAndProperties = injector.getInstance(Key.get(Cube.class, SessionWide.class));

        for (FieldDescriptor managedFieldDescriptor : reflection.getManagedFields()) {
            Class<?> fieldClass = managedFieldDescriptor.getFieldType();
            String fieldName = managedFieldDescriptor.getField().getName();

            //try to locate it from the cube by field name
            Class<?> pageClass = reflection.getPageClass();
            Object value = pagesAndProperties.get(pageClass, fieldName);

            //attempt to load it from the object if we dont have it
            Field field = managedFieldDescriptor.getField();
            if (null == value) {
                Object currentValue = ReflectUtils.readField(field, page);

                //if there is none, try to obtain it from guice via type
                if (null == currentValue) {
                    if (null == injector.getBinding(Key.get(fieldClass)))
                        IocContextManager.throwNotScopeableException("Cannot manage a property whose type has no guice binding: " + pageClass.getName() + "#" + field.getName());

                        value = injector.getInstance(fieldClass);

                    //got it, place it into the page object
                    ReflectUtils.writeField(field, page, value);
                } else  //use the current page value
                    value = currentValue;

                //store the new value (either created or obtained from page) in the cube
                pagesAndProperties.put(pageClass, fieldName, value);

            } else {
                //place cube's value into the page object
                ReflectUtils.writeField(field, page, value);
            }
        }
    }

    public synchronized void extractAndStore(PageClassReflection reflection, Object page) {
        Cube<Class<?>, String, Object> pagesAndProperties = injector.getInstance(Key.get(Cube.class, SessionWide.class));

        //end of the request, take whatever is in the object's managed prop and store it
        for (FieldDescriptor managedFieldDescriptor : reflection.getManagedFields()) {
            Object value = ReflectUtils.readField(managedFieldDescriptor.getField(), page);

            //store it in the cube overwriting whatever was there if any
            pagesAndProperties.put(reflection.getPageClass(), managedFieldDescriptor.getField().getName(), value);

            //TODO cascade the operation to any @Inject @Page fields
        }
    }

}
