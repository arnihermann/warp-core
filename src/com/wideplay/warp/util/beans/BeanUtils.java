package com.wideplay.warp.util.beans;

import ognl.Ognl;
import ognl.OgnlException;

/**
 * Created with IntelliJ IDEA.
 * On: 25/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class BeanUtils {
    public static Object getFromPropertyExpression(String expr, Object bean) {
        //lets use ognl to retrieve an expression instead of a prop
        try {
            return Ognl.getValue(expr, bean);
        } catch (OgnlException e) {
            throw new NotReadablePropertyException("Provided property expression has no getter or could not be applied to the provided object graph: " + expr, e);
        }
    }

    public static void setFromPropertyExpression(String expr, Object bean, Object value) {
        //lets use ognl to store an expression
        try {
            Ognl.setValue(expr, bean, value);
        } catch (OgnlException e) {
            throw new NotWritablePropertyException("Provided property expression has no setter or could not be applied to the provided object graph: " + expr, e);
        }
    }
}
