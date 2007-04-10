package com.wideplay.warp.module.ioc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.wideplay.warp.rendering.RequestBinder;
import ognl.Ognl;
import ognl.OgnlException;

import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

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
    private Logger log = Logger.getLogger(OgnlRequestBinder.class);

    @Inject
    public OgnlRequestBinder(@ReservedParameters Set<String> reservedParameterNames) {
        this.reservedParameterNames = reservedParameterNames;
    }

    public void bindBean(Object bean, Map<String, String[]> parameters) {

        log.debug("Binding "+parameters.size()+" params to a " + bean.getClass());

        //iterate the parameter set and bind the values to the provided bean
        for (String paramName : parameters.keySet()) {

            String contents="[";
            for (String var: parameters.get(paramName)) {

                if (contents.length()>1)
                    contents+=",";
                contents+="\""+var+"\"";
            }
            contents+="]";

            log.debug(paramName + " == " + contents);

            if (reservedParameterNames.contains(paramName))
                continue;

            //bind with a custom expression?
            if (EXPR_PARAMETER_NAME.equals(paramName)) {
                try {
                    for (String expression : parameters.get(paramName))
                        Ognl.getValue(expression, bean);
                    log.debug("bound via custom");
                    continue; //Goto considered harmful? :)
                } catch (OgnlException e) {
                    throw new RequestBindingException("Could not bind a request parameter (expression type) to the page object. Could be because of: a) missing setter, b) missing collection values, c) malformed request, or d) bug in the component that generated the binding expression", e);
                }
            }

            //or else bind normally via ognl
            try {
                log.debug("Binding via ognl");


                String value = parameters.get(paramName)[0];

                log.debug("Attempting to set " + paramName + " to " + value + " on " + bean);
                log.debug("old value = " + Ognl.getValue(paramName, bean));
                Ognl.setValue(paramName, bean, value);
                log.debug("new value = " + Ognl.getValue(paramName, bean));
            } catch (OgnlException e) {
                //TODO: ignore depending on reason... if it's simply unbound request data who cares?
                throw new RequestBindingException("Could not bind a request parameter to the page object. Could be because of: a) missing setter, b) malformed request, or c) bug in the component that generated the binding expression", e);
            }
        }
    }
}
