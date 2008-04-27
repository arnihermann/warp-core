package com.wideplay.warp.widgets.routing;

import com.wideplay.warp.widgets.RenderableWidget;
import com.google.inject.ImplementedBy;

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

        void doGet(Object page);

        void doPost(Object page);
    }
}
