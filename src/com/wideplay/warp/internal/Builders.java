package com.wideplay.warp.internal;

import com.wideplay.warp.WarpModule;
import com.wideplay.warp.module.WarpModuleAssembly;
import com.wideplay.warp.module.components.ClassReflectionCache;
import com.wideplay.warp.rendering.RequestBinder;
import com.wideplay.warp.util.reflect.ReflectUtils;

import javax.servlet.ServletContext;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * On: Mar 24, 2007 10:23:51 PM
 *
 * @author Dhanji R. Prasanna
 */
public class Builders {

    private Builders() {
    }

    public static WarpModuleAssembly buildWarpModuleAssembly(Class<WarpModule> moduleClass, ServletContext servletContext, String moduleRootDir, String modulePackage) {
        return new WarpModuleAssemblyBuilder(ReflectUtils.instantiate(moduleClass),servletContext).build(moduleRootDir, modulePackage);
    }

    public static Set<String> getReservedParameterNames() {
        Set<String> reserved = new HashSet<String>();
        reserved.add(RequestBinder.EVENT_PARAMETER_NAME);

        return reserved;
    }

    public static ClassReflectionCache newClassReflectionCache() {
        return new ClassReflectionCacheImpl();
    }
}
