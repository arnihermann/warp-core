package com.wideplay.warp.rendering;

import com.google.inject.Injector;
import com.wideplay.warp.module.pages.PageClassReflection;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * On: 30/08/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public interface RenderingContext {
    Map<String, Object> getContextVars();

    Injector getInjector();

    Object getPage();

    PageClassReflection getReflection();

    HtmlWriter getWriter();
}
