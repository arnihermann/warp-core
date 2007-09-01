package com.wideplay.warp.internal.pages;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.wideplay.warp.module.ComponentRegistry;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.rendering.HtmlWriter;
import com.wideplay.warp.rendering.PageHandler;
import com.wideplay.warp.rendering.RenderingContext;

import javax.servlet.ServletContext;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * On: Mar 24, 2007 10:38:12 PM
 *
 * @author Dhanji R. Prasanna
 */
public class PageBuilders {

    private PageBuilders() {
    }

    public static Module newPageServicesModule() {
        return new PageServicesGuiceModule();
    }

    public static void buildAndStorePageHandler(ServletContext context, ComponentRegistry registry, Class<?> pageClass, String packageName,
                                                Map<String, PageHandler> pages, Map<String, Object> pagesByTemplate) {
        new PageHandlerBuilder(context, registry).build(pageClass, packageName, pages, pagesByTemplate);

        //load resources
        //..
    }

    public static RenderingContext newRenderingContext(HtmlWriter writer, Injector injector, PageClassReflection reflection, Object contextObject) {
        return new RenderingContextImpl(writer, injector, reflection, contextObject);
    }
}
