package com.wideplay.warp.widgets.routing;

import com.google.inject.ImplementedBy;
import com.wideplay.warp.widgets.Renderable;

import java.util.Map;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@ImplementedBy(PageBookImpl.class)
public interface PageBook {
    Page at(String uri, Class<?> myPageClass);

    Page get(String uri);

    Page forName(String name);

    Page embedAs(Class<?> page);

    public static interface Page {
        Renderable widget();

        Object instantiate();

        Object doGet(Object page, String pathInfo, Map<String, String[]> params);

        Object doPost(Object page, String pathInfo, Map<String, String[]> params);

        Class<?> pageClass();

        void apply(Renderable widget);
    }
}
