package com.wideplay.warp.module.ioc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.wideplay.warp.rendering.RequestBinder;

import java.util.Map;
import java.util.Set;

import ognl.Ognl;
import ognl.OgnlException;

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
                try {
                    for (String expression : parameters.get(paramName))
                        Ognl.getValue(expression, bean);
                    return;
                } catch (OgnlException e) {
                    throw new RequestBindingException("Could not bind a request parameter (expression type) to the page object. Could be because of: a) missing setter, b) missing collection values, c) malformed request, or d) bug in the component that generated the binding expression", e);
                }
            }

            //or else bind normally via ognl
            try {
                Ognl.setValue(paramName, bean, parameters.get(paramName)[0]);
            } catch (OgnlException e) {
                throw new RequestBindingException("Could not bind a request parameter to the page object. Could be because of: a) missing setter, b) malformed request, or c) bug in the component that generated the binding expression", e);
            }
        }
    }
}
