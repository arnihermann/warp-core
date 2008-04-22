package com.wideplay.warp.widgets.binding;

import com.wideplay.warp.widgets.Evaluator;
import com.wideplay.warp.util.TextTools;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import org.mvel.PropertyAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
class ElRequestBinder implements RequestBinder {
    private final Evaluator evaluator;

    private static final String VALID_BINDING_REGEX = "[\\w\\.$]*";

    private final Logger log = LoggerFactory.getLogger(ElRequestBinder.class);

    public ElRequestBinder(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    public void bind(HttpServletRequest request, Object o) {
        @SuppressWarnings("unchecked")
        final Map<String, String[]> map = request.getParameterMap();

        //bind iteratively (last incoming param gets bound)
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            validate(entry.getKey());

            try {
                for (String value : entry.getValue())
                        evaluator.write(entry.getKey(), o, value);
            } catch (PropertyAccessException e) {
                //log missing property
            }
        }
    }

    private void validate(String binding) {
        if (TextTools.isEmptyString(binding)
                    || !binding.matches(VALID_BINDING_REGEX))
            throw new InvalidBindingException("Binding expression (form parameter) contained invalid characters: " + binding);
    }


}
