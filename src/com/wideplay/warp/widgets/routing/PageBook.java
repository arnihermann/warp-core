package com.wideplay.warp.widgets.routing;

import com.google.inject.ImplementedBy;
import com.wideplay.warp.widgets.RenderableWidget;

import java.util.Map;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@ImplementedBy(PageBookImpl.class)
public interface PageBook {
    void at(String uri, RenderableWidget page, Class<?> myPageClass);

    Page get(String uri);

    Page forName(String name);

    public static interface Page {
        RenderableWidget widget();

        Object instantiate();

        void doGet(Object page, String pathInfo, Map<String, String[]> params);

        void doPost(Object page, String pathInfo);
    }
}
