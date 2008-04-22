package com.wideplay.warp.widgets;

import org.jetbrains.annotations.Nullable;
import com.google.inject.ImplementedBy;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@ImplementedBy(MvelEvaluator.class)
public interface Evaluator {
    @Nullable
    Object evaluate(String expr, Object bean);

    void write(String expr, Object bean, Object value);

    Object read(String property, Object contextObject);
}
