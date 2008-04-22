package com.wideplay.warp.widgets;

import org.testng.annotations.Test;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class WidgetFilterTest {

    @Test
    public final void doFilter() throws IOException, ServletException {
        new WidgetFilter()
                .doFilter(null, null, null);
    }
}
