package com.wideplay.warp.internal;

import com.google.inject.Provider;
import com.wideplay.warp.module.WarpModuleAssembly;

/**
 * Created by IntelliJ IDEA.
 * User: dprasanna
 * Date: 29/03/2007
 * Time: 12:05:39
 * <p/>
 *
 * Singleton provider obtains the current module assembly.
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
class WarpModuleAssemblyProvider implements Provider<WarpModuleAssembly> {
    private WarpModuleAssembly assembly;

    public WarpModuleAssembly get() {
        return assembly;
    }


    void setAssembly(WarpModuleAssembly assembly) {
        this.assembly = assembly;
    }
}
