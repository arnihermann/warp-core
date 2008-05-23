package com.wideplay.warp.widgets.routing;

import com.google.inject.ImplementedBy;
import com.wideplay.warp.widgets.Renderable;

import java.util.Map;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@ImplementedBy(PageBookImpl.class)
public interface PageBook {
    void at(String uri, Renderable page, Class<?> myPageClass);

    Page get(String uri);

    Page forName(String name);

    void embedAs(Renderable renderable, Class<?> page);

    public static interface Page {
        Renderable widget();

        Object instantiate();

        Object doGet(Object page, String pathInfo, Map<String, String[]> params);

        Object doPost(Object page, String pathInfo, Map<String, String[]> params);
    }
}
