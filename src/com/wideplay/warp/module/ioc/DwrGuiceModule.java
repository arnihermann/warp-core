package com.wideplay.warp.module.ioc;

import com.google.inject.Singleton;
import com.wideplay.warp.rendering.HtmlWriter;
import org.directwebremoting.guice.AbstractDwrModule;

/**
 * Created with IntelliJ IDEA.
 * On: 27/08/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
class DwrGuiceModule extends AbstractDwrModule {

    protected void configure() {
        bindRemoted(RemoteEventProxy.class).to(DwrRemoteEventProxyImpl.class).in(Singleton.class);

        bind(HtmlWriter.class).annotatedWith(DwrResponse.class).to(DwrResponseHtmlWriter.class);

//        bindDwrScopes();
    }
}
