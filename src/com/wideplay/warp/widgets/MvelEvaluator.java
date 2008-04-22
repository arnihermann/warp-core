package com.wideplay.warp.widgets;

import com.wideplay.warp.util.TextTools;
import com.wideplay.warp.util.Token;

import java.io.Serializable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;

import org.mvel.MVEL;
import org.mvel.PropertyAccessException;
import org.jetbrains.annotations.Nullable;
import net.jcip.annotations.ThreadSafe;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@ThreadSafe
class MvelEvaluator implements Evaluator {

    //lets do some caching of expressions to see if we cant go a bit faster
    private final ConcurrentMap<String, Serializable> compiledExpressions =
            new ConcurrentHashMap<String, Serializable>();

    @Nullable
    public Object evaluate(String expr, Object bean) {
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

        //lets use mvel to retrieve an expression value instead of a prop
        try {
            return MVEL.executeExpression(compiled, bean);
        } catch(PropertyAccessException e) {
            throw new IllegalArgumentException(String.format("Could not read property from expression %s (missing a getter?)", expr), e);
        } catch(NullPointerException npe) {
            throw new IllegalArgumentException(String.format("Evaluation of property expression [%s] resulted in a NullPointerException", expr), npe);
        }
    }


    public void write(String expr, Object bean, Object value) {
        //lets use mvel to store an expression
        MVEL.setProperty(bean, expr, value);
    }

    public Object read(String property, Object contextObject) {
        return MVEL.getProperty(property, contextObject);
    }

}
