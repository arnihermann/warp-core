package com.wideplay.warp.internal.pages;

import com.google.inject.AbstractModule;
import com.wideplay.warp.rendering.HtmlWriter;

/**
 * Created with IntelliJ IDEA.
 * On: 28/08/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
class PageServicesGuiceModule extends AbstractModule {

    protected void configure() {
        bind(HtmlWriter.class).annotatedWith(JsFrame.class).to(JsFrameHtmlWriter.class);
    }
}
