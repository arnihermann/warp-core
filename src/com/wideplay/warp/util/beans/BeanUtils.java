package com.wideplay.warp.util.beans;

import org.mvel.MVEL;
import org.mvel.PropertyAccessException;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.Map;
import java.util.WeakHashMap;

import com.wideplay.warp.util.TextTools;

/**
 * Created with IntelliJ IDEA.
 * On: 25/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class BeanUtils {

    //lets do some caching of expressions to see if we cant go a bit faster
    private static final Map<String, Serializable> compiledExpressions = new WeakHashMap<String, Serializable>();

    public static Object getFromPropertyExpression(String expr, Object bean) {
        Serializable compiled = compiledExpressions.get(expr);

        //compile and store the expr (warms up the expression cache)
        if (null == compiled) {
            String preparedExpression = expr;

            //strip expression decorators as necessary
            if (TextTools.isExpression(expr)) {
                preparedExpression = TextTools.stripExpression(expr);
            }

            //compile expression
            compiled = MVEL.compileExpression(preparedExpression);

            //place into map under original key (i.e. as it came in)
            compiledExpressions.put(expr, compiled);
        }

        //lets use mvel to retrieve an expression instead of a prop
        return MVEL.executeExpression(compiled, bean);
    }

    public static void setFromPropertyExpression(String expr, Object bean, Object value) {
        //lets use mvel to store an expression
        MVEL.setProperty(bean, expr, value);
    }
}
