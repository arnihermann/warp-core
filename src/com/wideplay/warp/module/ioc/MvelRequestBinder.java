package com.wideplay.warp.module.ioc;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestParameters;
import com.wideplay.warp.module.ioc.el.Expressions;
import com.wideplay.warp.rendering.RequestBinder;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: dhanji
 * Date: Aug 10, 2007
 * Time: 3:32:17 PM
 */
class MvelRequestBinder implements RequestBinder {
    private final Set<String> reservedParameterNames;
    
    private static final String COLLECTION_BIND_DELIMITER = "_c_";

    @Inject
    public MvelRequestBinder(@ReservedParameters Set<String> reservedParameterNames) {
        this.reservedParameterNames = reservedParameterNames;
    }


    public String createCollectionBindingExpression(String items, String bind) {
        return String.format("%s%s%s%s", COLLECTION_BIND_DELIMITER, items, COLLECTION_BIND_DELIMITER, bind);
    }

    public void bindObject(Object bean, @RequestParameters Map<String, String[]> parameters) {
        //iterate the parameter set and bind the values to the provided bean
        for (String paramName : parameters.keySet()) {

            if (reservedParameterNames.contains(paramName))
                continue;


            //should we bind this from a collection?
            if (null != paramName && paramName.startsWith(COLLECTION_BIND_DELIMITER)) {
                for (String value : parameters.get(paramName))
                    bindFromCollection(paramName, bean, value);
                continue;
            }

            //or else bind normally via mvel
            for (String value : parameters.get(paramName))
                bindAsProperty(paramName, value, bean);
        }
    }

    public void bindBeanFromMap(Object bean, @RequestParameters Map<String, String> parameters) {
        //iterate the parameter set and bind the values to the provided bean
        for (String paramName : parameters.keySet()) {

            if (reservedParameterNames.contains(paramName))
                continue;

            if (null != paramName && paramName.startsWith(COLLECTION_BIND_DELIMITER)) {
                bindFromCollection(paramName, bean, parameters.get(paramName));
                continue;
            }

            //or else bind normally via mvel
            bindAsProperty(paramName, parameters.get(paramName), bean);
        }
    }

    private void bindFromCollection(String paramName, Object bean, String value) {
        String[] collectionBindExpression = paramName.split(COLLECTION_BIND_DELIMITER);

        //validate
        if (3 != collectionBindExpression.length)
            throw new RequestBindingException("Improper collection pathing expression, must contain collection and property path, with delimiters (malformed request or badly written component?): " + paramName);

        final Integer hashSelected = parseInt(value);

        //discard 1st delimiter, 2nd element is collection path
        Object collection = Expressions.evaluate(collectionBindExpression[1], bean);

        if (collection instanceof Collection)
            bindFromJUCollection(collectionBindExpression, bean, hashSelected, paramName);
        else if (collection instanceof Object[])
            bindFromArray(collectionBindExpression, bean, hashSelected, paramName);
        else
            throw new RequestBindingException("Improper collection pathing expression, items must resolve to a collection or array: " + collectionBindExpression[1]);
    }

    private void bindFromArray(String[] collectionBindExpression, Object bean, final Integer hashSelected, String paramName) {
        Object[] col = (Object[]) Expressions.evaluate(collectionBindExpression[1], bean);

        //selection is looked up by hashcode && locate the object now
        Object selected = selectClosureOverArray(col, hashSelected);

        //if everything's ok, 3rd element is target property path
        Expressions.write(collectionBindExpression[2], bean, selected);
    }

    private void bindFromJUCollection(String[] collectionBindExpression, Object bean, final Integer hashSelected, String paramName) {
        Collection<?> col = (Collection<?>) Expressions.evaluate(collectionBindExpression[1], bean);

        //selection is looked up by hashcode
        //noinspection SuspiciousMethodCalls
        if (!col.contains(new Object() {

            @Override
            public boolean equals(Object object) {
                return null != object && object.hashCode() == hashCode();
            }

            @Override
            public int hashCode() {
                return hashSelected;
            }
        }))
                throw new RequestBindingException("Improper collection pathing expression, selected value was not present in collection (malformed request, badly written component or concurrent modification of collection values?): " + paramName);

        //locate the object now
        Object selected = selectClosureOverCollection(col, hashSelected);

        //if everything's ok, 3rd element is target property path
        Expressions.write(collectionBindExpression[2], bean, selected);
    }

    private Object selectClosureOverCollection(Collection<?> col, Integer hashSelected) {
        for (Object item : col) {
            if (hashSelected == item.hashCode()) {
                return item;
            }
        }

        throw new RequestBindingException("Improper collection pathing expression, selected value was not present in collection (malformed request, badly written component or concurrent modification of collection values?): " + col);
    }

    private Object selectClosureOverArray(Object[] col, Integer hashSelected) {
        for (Object item : col) {
            if (hashSelected == item.hashCode()) {
                return item;
            }
        }

        throw new RequestBindingException("Improper collection pathing expression, selected value was not present in collection (malformed request, badly written component or concurrent modification of collection values?): " + col);
    }

    private Integer parseInt(String value) {
        try {
            return Integer.valueOf(value);
        } catch(NumberFormatException nfe) {
            throw new RequestBindingException("Improper collection pathing expression, option value must contain integer hashcode of the selected object: " + value, nfe);
        }
    }

    private void bindAsProperty(String paramName, String value, Object bean) {
        Expressions.write(paramName, bean, value);
    }
}
