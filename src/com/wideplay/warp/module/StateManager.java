package com.wideplay.warp.module;

import com.google.inject.Injector;
import com.wideplay.warp.module.pages.PageClassReflection;

/**
 * Created by IntelliJ IDEA.
 * User: dprasanna
 * Date: 28/03/2007
 * Time: 14:51:21
 * <p/>
 * TODO: Describe me!
 *
 * @author dprasanna
 * @since 1.0
 */
public interface StateManager {
    void injectManaged(Injector injector, PageClassReflection reflection, Object page);

    void extractAndStore(PageClassReflection reflection, Object page);
}
