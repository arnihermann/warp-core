package com.wideplay.warp.widgets.routing;

import com.google.inject.ImplementedBy;
import com.wideplay.warp.widgets.rendering.CompileError;

import java.util.List;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@ImplementedBy(DefaultSystemMetrics.class)
public interface SystemMetrics {
    //records an additional page render time for the given page (in millis)
    void logPageRenderTime(Class<?> page, long time);

    void logErrorsAndWarnings(Class<?> page, List<CompileError> errors, List<CompileError> warnings);
}
