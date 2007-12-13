package com.wideplay.warp;

import com.google.inject.Key;
import com.google.inject.Module;
import com.wideplay.warp.rendering.templating.HtmlElementFilter;

/**
 * Created with IntelliJ IDEA.
 * On: 23/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
public interface Warp {
    void install(Module...guiceModule);

    void addStartupListener(Class<? extends StartupListener> startupListener);
    void addStartupListener(Key<? extends StartupListener> startupListener);

    void setUrlEncoding(String scheme);

    void addShutdownListener(Class<? extends ShutdownListener> shutdownListener);
    void addShutdownListener(Key<? extends ShutdownListener> shutdownListener);


    HeaderFiltersBuilder headers();

    public static interface HeaderFiltersBuilder {
        HeaderFiltersBuilder filter(Class<? extends HtmlElementFilter> filter);
        HeaderFiltersBuilder filter(Key<? extends HtmlElementFilter> filter);
    }
}
