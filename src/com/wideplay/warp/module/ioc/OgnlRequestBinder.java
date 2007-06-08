package com.wideplay.warp.module.ioc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.wideplay.warp.rendering.RequestBinder;
import ognl.DefaultTypeConverter;
import ognl.Ognl;
import ognl.OgnlException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * On: 25/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
@Singleton
class OgnlRequestBinder implements RequestBinder {

    private final Set<String> reservedParameterNames;

    //create a new context-less default context
    @SuppressWarnings("unchecked")
    private static final Map<String, Object> defaultOgnlContext = Ognl.createDefaultContext(new Object());

    static {
        //setup Ognl default type converter (treat "true" and "false" string literals as booleans)
        Ognl.setTypeConverter(defaultOgnlContext, new DefaultTypeConverter() {

            public Object convertValue(Map map, Object value, Class toType) {
                Object result;
                
                //coerce any string into a boolean (or delegate to the default type converter)
                if ((boolean.class == toType || Boolean.class == toType) && value instanceof String) {
                    result = Boolean.valueOf((String)value);
                }
                else
                    result = super.convertValue(map, value, toType);

                return result;
            }
        });
    }

    private final Log log = LogFactory.getLog(OgnlRequestBinder.class);

    @Inject
    public OgnlRequestBinder(@ReservedParameters Set<String> reservedParameterNames) {
        this.reservedParameterNames = reservedParameterNames;
    }

    public void bindBean(Object bean, Map<String, String[]> parameters) {

        //iterate the parameter set and bind the values to the provided bean
        for (String paramName : parameters.keySet()) {

            if (reservedParameterNames.contains(paramName))
                continue;

            //bind with a custom expression?
            if (EXPR_PARAMETER_NAME.equals(paramName)) {
                bindAsExpression(parameters, paramName, bean);
                continue;
            }

            //or else bind normally via ognl
            for (String value : parameters.get(paramName))
                bindAsProperty(paramName, value, bean);
        }
    }

    private void bindAsProperty(String paramName, String value, Object bean) {
        try {
            //set the value of the parameter to the appropriate bean path
            Ognl.setValue(paramName, defaultOgnlContext, bean, value);

        } catch (OgnlException e) {
            //TODO: ignore depending on reason... if it's simply unbound request data who cares?
            throw new RequestBindingException("Could not bind a request parameter to the page object. Could be because of: a) missing setter, b) malformed request, or c) bug in the component that generated the binding expression", e);

        }
    }

    private void bindAsExpression(Map<String, String[]> parameters, String paramName, Object bean) {
        try {
            for (String expression : parameters.get(paramName))
                Ognl.getValue(expression, bean);
        } catch (OgnlException e) {
            throw new RequestBindingException("Could not bind a request parameter (expression type) to the page object. Could be because of: a) missing setter, b) missing collection values, c) malformed request, or d) bug in the component that generated the binding expression", e);
        }
    }
}
