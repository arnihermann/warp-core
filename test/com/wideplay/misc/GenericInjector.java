package com.wideplay.misc;

import com.google.inject.Inject;
import com.google.inject.TypeLiteral;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
public class GenericInjector {

    private final Map<TypeLiteral<?>, Object> bindings = new HashMap<TypeLiteral<?>, Object>();
    private final Stack<Type> witness = new Stack<Type>();

    public <T> void bind(TypeLiteral<T> type, T value) {
        bindings.put(type, value);
    }

    public void inject(Object obj) throws IllegalAccessException {
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class))
                dependencyInject(field, obj);
        }

        if (!witness.empty())
            witness.pop();
    }

    private void dependencyInject(Field field, Object obj) throws IllegalAccessException {
        Object injected = null;

        //first see if field's type is bound to a normal type
        if (field.getGenericType() instanceof Class)
            injected = injectNormal(field, obj);
        else if (field.getGenericType() instanceof ParameterizedType) {
            if (((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0] instanceof TypeVariable)
                injected = injectVariableType(field, obj);
            else
                injected = injectParameterized(field, obj);
        }

        //now recursively inject all the deps of this created object
        if (isInjectable(injected))
            inject(injected);

        //no bindings could be found =(
        if (null == injected)
            throw new RuntimeException(String.format("No type binding available for injection point %s.%s ",
                    field.getDeclaringClass().getSimpleName(), field.getName()));
    }

    private Object injectVariableType(Field field, Object obj) throws IllegalAccessException {
        //determine best match in bindings for the current parameterized type
        for (Map.Entry<TypeLiteral<?>, Object> typeLiteral : bindings.entrySet()) {
            final Type type = typeLiteral.getKey().getType();

            //only match against parameterized type literal bindings
            if (type instanceof ParameterizedType) {
                final ParameterizedType parameterizedType = (ParameterizedType) type;

                //if raw types match
                if (parameterizedType.getRawType().equals(field.getType())) {

                    //now test if the parameterized type argument matches the top of the witness stack
                    if (parameterizedType.getActualTypeArguments()[0].equals(witness.peek())) {
                        return set(field, obj, typeLiteral.getValue());
                    }
                }
            }
        }

        return null;
    }


    static final List<Class<?>> UNINJECTABLES = Arrays.asList(String.class, List.class);  // or other scalar types
    private static boolean isInjectable(Object injected) {
        if (null == injected)
            return false;
        
        for (Class<?> uninjectable : UNINJECTABLES) {
            if (uninjectable.isInstance(injected))
                return false;
        }
        return true;
    }

    private Object injectParameterized(Field field, Object obj) throws IllegalAccessException {
        //safe cast ensured by caller
        final ParameterizedType type = (ParameterizedType) field.getGenericType();

        //witness the current generic parameter
        witness.push(type.getActualTypeArguments()[0]);

        for (Map.Entry<TypeLiteral<?>, Object> typeLiteral : bindings.entrySet())
            if (typeLiteral.getKey().getType().equals(type))
                return set(field, obj, typeLiteral.getValue());
                        
        //not injectable!
        return null;
    }


    private Object injectNormal(Field field, Object obj) throws IllegalAccessException {
        for (Map.Entry<TypeLiteral<?>,Object> typeLiteral : bindings.entrySet())
            if (field.getType().equals(typeLiteral.getKey().getType()))
                return set(field, obj, typeLiteral.getValue());

        //not injectable!
        return null;
    }


    private static Object set(Field field, Object obj, Object value) throws IllegalAccessException {
        //blech...
        if (!field.isAccessible())
            field.setAccessible(true);

        field.set(obj, value);
        return value;
    }

    public List<String> list() {
        return new ArrayList<String>();
    }
}
