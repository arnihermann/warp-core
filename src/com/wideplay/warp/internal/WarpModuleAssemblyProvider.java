package com.wideplay.warp.internal;

import com.wideplay.warp.module.WarpModuleAssembly;
import com.google.inject.Provider;

/**
 * Created by IntelliJ IDEA.
 * User: dprasanna
 * Date: 29/03/2007
 * Time: 12:05:39
 * <p/>
 *
 * Singleton provider obtains the current module assembly.
 *
 * @author dprasanna
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
