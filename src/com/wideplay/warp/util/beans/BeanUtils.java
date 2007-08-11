package com.wideplay.warp.util.beans;

import org.mvel.MVEL;

/**
 * Created with IntelliJ IDEA.
 * On: 25/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class BeanUtils {
    public static Object getFromPropertyExpression(String expr, Object bean) {
        //lets use mvel to retrieve an expression instead of a prop
        return MVEL.eval(expr, bean);
    }

    public static void setFromPropertyExpression(String expr, Object bean, Object value) {
        //lets use mvel to store an expression
        MVEL.setProperty(bean, expr, value);
    }
}
