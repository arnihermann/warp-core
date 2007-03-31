package com.wideplay.warp;

import com.google.inject.Module;

/**
 * Created with IntelliJ IDEA.
 * On: 23/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public interface Warp {
    void install(Module guiceModule);
}
