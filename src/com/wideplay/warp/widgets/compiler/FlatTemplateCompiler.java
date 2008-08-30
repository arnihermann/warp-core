package com.wideplay.warp.widgets.compiler;

import com.wideplay.warp.widgets.Renderable;
import com.wideplay.warp.widgets.rendering.control.WidgetRegistry;
import com.wideplay.warp.widgets.routing.SystemMetrics;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Compiles non-XML templates.
 *
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 * @see XmlTemplateCompiler
 */
class FlatTemplateCompiler {
    private final Class<?> page;
    private final MvelEvaluatorCompiler compiler;
    private final SystemMetrics metrics;
    private final WidgetRegistry registry;

    public FlatTemplateCompiler(Class<?> page, MvelEvaluatorCompiler compiler, SystemMetrics metrics, WidgetRegistry registry) {
        this.page = page;
        this.compiler = compiler;
        this.metrics = metrics;
        this.registry = registry;
    }

    public Renderable compile(String template) {
        //TODO hacky! Change this to run only once if possible
        template = template.substring(Parsing.indexOfMeta(template) + "@Meta".length());

        try {
            return registry.textWidget(template, compiler);

        } catch (ExpressionCompileException e) {
            final List<CompileError> errors = Arrays.asList(
                    CompileError.in(template)
                            .near(e.getError().getError().getRow())
                            .causedBy(e)
            );

            final List<CompileError> warnings = Collections.emptyList();

            //log errors and abort compile
            metrics.logErrorsAndWarnings(page, errors, warnings);

            throw new TemplateCompileException(page, template, errors, warnings);
        }
    }
}
