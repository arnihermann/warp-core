package com.wideplay.warp.widgets.rendering.control;

import com.google.inject.ImplementedBy;
import com.wideplay.warp.widgets.Renderable;
import com.wideplay.warp.widgets.rendering.EvaluatorCompiler;
import com.wideplay.warp.widgets.rendering.ExpressionCompileException;
import com.wideplay.warp.widgets.rendering.RepeatToken;

import java.util.Map;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
@ImplementedBy(DefaultWidgetRegistry.class)
public interface WidgetRegistry {
    void add(String key, Class<? extends Renderable> widget);

    boolean isSelfRendering(String widget);

    RepeatToken parseRepeat(String expression);

    XmlWidget xmlWidget(WidgetChain childsChildren, String elementName, Map<String, String> attribs,
                                EvaluatorCompiler compiler) throws ExpressionCompileException;

    Renderable newWidget(String key, String expression, WidgetChain widgetChain, EvaluatorCompiler compiler)
                                        throws ExpressionCompileException;

    Renderable requireWidget(String template, EvaluatorCompiler compiler) throws ExpressionCompileException;

    Renderable textWidget(String template, EvaluatorCompiler compiler) throws ExpressionCompileException;

    void addEmbed(String embedAs);

    void addArgument(String callWith);
}
