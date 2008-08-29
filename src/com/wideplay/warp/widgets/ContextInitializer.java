package com.wideplay.warp.widgets;

import com.google.inject.Inject;
import net.jcip.annotations.NotThreadSafe;

import javax.servlet.ServletContext;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@NotThreadSafe
class ContextInitializer {
    private final WidgetScanner scanner;

    @Inject
    public ContextInitializer(WidgetScanner scanner) {
        this.scanner = scanner;
    }

    public void init(ServletContext servletContext) {

        //scan and add pages/widgets
        scanner.scan();
    }
}
