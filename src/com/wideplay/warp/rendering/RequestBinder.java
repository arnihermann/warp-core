package com.wideplay.warp.rendering;

import com.google.inject.servlet.RequestParameters;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * On: 25/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
public interface RequestBinder {
    String EVENT_PARAMETER_NAME = "w_event";
    String EVENT_TOPIC_PARAMETER_NAME = "w_event_topic";
//    String EXPR_PARAMETER_NAME = "__eval_as_expr_";

    void bindBean(Object bean, @RequestParameters Map<String, String[]> parameters);

    void bindBeanFromMap(Object bean, @RequestParameters Map<String, String> parameters);

    String createCollectionBindingExpression(String items, String bind);
}
