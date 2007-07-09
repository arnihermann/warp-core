package com.wideplay.warp.module.ioc;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.wideplay.warp.module.StateManager;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.util.Cube;
import com.wideplay.warp.util.reflect.FieldDescriptor;
import com.wideplay.warp.util.reflect.ReflectUtils;

import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * On: 20/03/2007
 *
 * This should never be a singleton, only a session-scoped object or less (whatever scopes @Managed allows).
 * 
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
class StateManagerImpl implements StateManager {
    private final Injector injector;

    @Inject public StateManagerImpl(Injector injector) {
        this.injector = injector;
    }

    //TODO add additional cubes at different scopes?
    //TODO fix generics
    /**
     * Performs a sequence of state management operations at the *beginning* of a request on a given page:
     *
     *  - looks up all @Managed properties of the page instance and checks if they are available from a previous request
     *  - if none, checks if the user/instance itself has created a value (perhaps in the ctor?)
     *  - if not, try to obtain a bound value of the field from the guice injector
     *  - If necessary, place the value into the page and back into the cube
     *
     * @param reflection Reflection cache of the page class to inject @Managed properties of
     * @param page The page object instance
     */
    public synchronized void injectManaged(PageClassReflection reflection, Object page) {
        //lookup scoped cube (repository of all managed properties keyed by page and field)
        Cube<Class<?>, String, Object> pagesAndProperties = injector.getInstance(Key.get(Cube.class, SessionWide.class));

        //go over each @Managed field and restore it from the cube or else obtain from injector, inject it & and store into cube
        for (FieldDescriptor managedFieldDescriptor : reflection.getManagedFields()) {
            Class<?> fieldClass = managedFieldDescriptor.getFieldType();
            String fieldName = managedFieldDescriptor.getField().getName();

            //try to locate it from the cube by field name
            Class<?> pageClass = reflection.getPageClass();
            Object value = pagesAndProperties.get(pageClass, fieldName);

            //cube didnt have it, so attempt to load it from the object (if user-created)
            Field field = managedFieldDescriptor.getField();
            if (null == value) {
                Object currentValue = ReflectUtils.readField(field, page);

                //if there is none, try to obtain it from guice via binding
                if (null == currentValue) {
                    if (null == injector.getBinding(Key.get(fieldClass)))
                        IocContextManager.throwNotScopeableException(String.format("Cannot manage a property whose type has no guice binding: %s.%s", pageClass.getName(), field.getName()));

                        value = injector.getInstance(fieldClass);

                    //got it from injector, place it into the page object
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

    /**
     * This method does the complement of injectManaged(). It loads @Managed properties from the given page
     * object and stores them into the cube so injectManaged() can obtain them in subsequent requests.
     *
     * If there is no value in the field (has been set to null), clears the corresponding slot in the cube, so
     * a new value is obtained the next request (if @Managed is autoCreate=true has been set).
     *
     * @param reflection Reflection cache of the page class to read @Managed properties of
     * @param page The page object instance
     */
    public synchronized void extractAndStore(PageClassReflection reflection, Object page) {
        //TODO fix generics
        Cube<Class<?>, String, Object> pagesAndPropertiesCube = injector.getInstance(Key.get(Cube.class, SessionWide.class));

        //end of the request, take whatever is in the object's managed prop and store it
        for (FieldDescriptor managedFieldDescriptor : reflection.getManagedFields()) {
            Object value = ReflectUtils.readField(managedFieldDescriptor.getField(), page);

            //store it in the cube overwriting whatever was there if any
            pagesAndPropertiesCube.put(reflection.getPageClass(), managedFieldDescriptor.getField().getName(), value);
        }
    }

}
