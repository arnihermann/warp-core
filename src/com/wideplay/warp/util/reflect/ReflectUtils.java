package com.wideplay.warp.util.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * On: 19/02/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class ReflectUtils {


    private ReflectUtils() {
    }

    public static <T> T instantiate(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("cannot instantiate object, probably because there is no nullary ctor", e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("cannot instantiate bean due to access visibility", e);
        }
    }

    public static <T> T instantiate(Constructor<T> constructor, Object[] params) {
        try {
            return constructor.newInstance(params);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("cannot instantiate object, probably because there is no nullary ctor", e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("cannot instantiate bean due to access visibility", e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("cannot instantiate bean because its constructor thre an exception", e);
        }
    }

    static <T extends Exception> T makeException(Class<T> clazz, String arg) {
        try {
            return clazz.getConstructor(new Class[] { String.class }).newInstance(new Object[] { arg});
        } catch (InstantiationException e) {
            throw new InfrastructureError("fatal: could not instantiate requested exception: " + clazz, e);
        } catch (NoSuchMethodException e) {
            throw new InfrastructureError("fatal: could not instantiate requested exception: " + clazz, e);
        } catch (IllegalAccessException e) {
            throw new InfrastructureError("fatal: could not instantiate requested exception: " + clazz, e);
        } catch (InvocationTargetException e) {
            throw new InfrastructureError("fatal: could not instantiate requested exception: " + clazz, e);
        }
    }
   
    public static Object invokeMethod(Method m, Object bean, Object[] args) {
        try {
            return m.invoke(bean, args);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("cannot invoke method due to access visibility", e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("cannot complete invoke method due to it throwing an exception", e);
        }
    }

    public static Object readField(Field f, Object bean) {
        try {
            return f.get(bean);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("cannot read field due to access visibility", e);
        }
    }

    public static void writeField(Field f, Object bean, Object value) {
        try {
            f.set(bean, value);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("cannot read field due to access visibility", e);
        }
    }

    public static Object invokeThrowingMethod(Method m, Object bean, Object[] args) throws InvocationTargetException {
        try {
            return m.invoke(bean, args);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("cannot invoke method due to access visibility", e);
        }
    }

    @SuppressWarnings("unchecked")
    static <E> Class<? super E> findInterfaceWithAnnotation(Class<E> base, Class<? extends Annotation> annotation) {
        //walk all interfaces of base and determine if they are marked with annotation
        for (Class<? super E> iface : base.getInterfaces())
            if (iface.isAnnotationPresent(annotation))
                return iface;

        //nothing found
        return null;
    }

    static MethodDescriptor findMethodDescriptorWithAnnotation(Class<?> base, Class<? extends Annotation> annotation) {
        //walk all methods of base and determine if they are marked with annotation
        for (Method method : base.getMethods())
            if (method.isAnnotationPresent(annotation))
                return new MethodDescriptor(annotation, method.getAnnotation(annotation), method);

        //nothing found
        return null;
    }

    static List<MethodDescriptor> findPublicMethodDescriptorsWithAnnotation(Class<?> base, Class<? extends Annotation> annotation) {
        List<MethodDescriptor> annotatedMethods = new LinkedList<MethodDescriptor>();

        //walk all methods of base and determine if they are marked with annotation
        for (Method method : base.getMethods())
            if (method.isAnnotationPresent(annotation))
                annotatedMethods.add(new MethodDescriptor(annotation, method.getAnnotation(annotation), method));

        //nothing found
        return annotatedMethods;
    }

    static Method findPublicMethodWithAnnotation(Class<?> base, Class<? extends Annotation> annotation) {
        //walk all methods of base and determine if they are marked with annotation
        for (Method method : base.getMethods())
            if (method.isAnnotationPresent(annotation))
                return method;

        //nothing found
        return null;
    }



    static Method[] findMethodsWithAnnotation(Class<?> base, Class<? extends Annotation> annotation) {
        List<Method> annotatedMethods = new LinkedList<Method>();

        //walk all methods of base and determine if they are marked with annotation
        for (Method method : base.getDeclaredMethods())
            if (method.isAnnotationPresent(annotation))
                annotatedMethods.add(method);

        //return found as array
        return annotatedMethods.toArray(new Method[annotatedMethods.size()]);
    }


    public static List<Method> findDeclaredMethodsWithAnnotationAsList(Class<?> base, Class<? extends Annotation> annotation) {
        List<Method> annotatedMethods = new LinkedList<Method>();

        //walk all methods of base and determine if they are marked with annotation
        for (Method method : base.getDeclaredMethods())
            if (method.isAnnotationPresent(annotation))
                annotatedMethods.add(method);

        //return found as array
        return annotatedMethods;
    }


    @SuppressWarnings("unchecked")
    //returns the first constructor with the matching annotation or null (does not search up the class hierarchy)
    public static <T> Constructor<T> findDeclaredConstructorWithAnnotation(Class<T> base, Class<? extends Annotation> annotation) {
        for (Constructor<T> constructor : base.getDeclaredConstructors())
            if (constructor.isAnnotationPresent(annotation))
                return constructor;

        return null;
    }

    public static Set<FieldDescriptor> findDeclaredFieldDescriptorsWithAnnotation(Class<?> base, Class<? extends Annotation> annotationClass) {
        Set<FieldDescriptor> annotatedFields = new LinkedHashSet<FieldDescriptor>();

        //walk all methods of base and determine if they are marked with annotation
        for (Field field : base.getDeclaredFields())
            if (field.isAnnotationPresent(annotationClass))
                annotatedFields.add(new FieldDescriptor(annotationClass, field.getAnnotation(annotationClass), field));

        //return found map
        return annotatedFields;
    }

    static Method findMethod(Object bean, String methodName, Class[] argTypes) {
        try {
            return bean.getClass().getMethod(methodName, argTypes);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("no method found for signal dispatch! (version mismatch? message corruption?)", e);
        }
    }

    //converts a qualified annotation class name to @Blah
    public static String extractAnnotationSimpleName(Class<? extends Annotation> clazz) {
        String simpleName = clazz.getName().substring(clazz.getName().lastIndexOf('.') + 1);

        return "@" + simpleName;
    }

    //converts a getter or setter to a property name TODO change to use String.format
    public static String extractPropertyNameFromAccessor(String accessor) {
        return Character.toLowerCase(accessor.charAt(3)) + accessor.substring(4);
    }


    public static void makeMethodsAccessible(Collection<Method> methods) {
        for (Method method : methods)
            if (!method.isAccessible())
                method.setAccessible(true);
    }

    public static void makeFieldsAccessible(Collection<Field> fields) {
        for (Field field : fields)
            if (!field.isAccessible())
                field.setAccessible(true);
    }

    public static void makeConstructorsAccessible(Collection<Constructor> constructors) {
        for (Constructor constructor : constructors)
            if (!constructor.isAccessible())
                constructor.setAccessible(true);
    }

    public static Constructor findNullaryConstructor(Class<?> pageClass) {
        for (Constructor constructor : pageClass.getDeclaredConstructors()) {
            if (constructor.getParameterTypes().length == 0)
                return constructor;
        }

        return null;
    }

    public static void makeMemberAccessible(AccessibleObject injectable) {
        if (injectable.isAccessible())
            injectable.setAccessible(true);
    }

    public static void makeFieldDescriptorsAccessible(Collection<FieldDescriptor> fieldDescriptors) {
        for (FieldDescriptor descriptor : fieldDescriptors)
            if (!descriptor.getField().isAccessible())
                descriptor.getField().setAccessible(true);
    }
}
