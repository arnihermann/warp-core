package com.wideplay.warp.rendering;

import com.google.inject.Injector;
import com.wideplay.warp.module.pages.PageClassReflection;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * On: Mar 24, 2007 7:20:44 PM
 *
 * @author Dhanji R. Prasanna
 */
public interface ComponentHandler {
    void handleRender(HtmlWriter writer, Injector injector, PageClassReflection pageReflection, Object page);

    List<? extends ComponentHandler> getNestedComponents();
}
