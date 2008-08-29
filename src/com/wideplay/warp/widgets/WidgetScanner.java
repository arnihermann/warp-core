package com.wideplay.warp.widgets;

import com.google.inject.ImplementedBy;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
@ImplementedBy(ClasspathWidgetScanner.class)
interface WidgetScanner {
    void scan();
}
