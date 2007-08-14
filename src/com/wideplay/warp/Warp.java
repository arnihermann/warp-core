package com.wideplay.warp;

import com.google.inject.Module;
import com.google.inject.Key;

/**
 * Created with IntelliJ IDEA.
 * On: 23/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public interface Warp {
    void install(Module guiceModule);

    void addStartupListener(Class<? extends StartupListener> startupListener);
    void addStartupListener(Key<? extends StartupListener> startupListener);

    void setUrlEncoding(String scheme);
}
