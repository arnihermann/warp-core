package com.wideplay.warp.module.ioc;

import com.google.inject.Provider;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * On: 25/03/2007
 *
 * TODO why is this class final (it is package local anyway)?
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
final class RequestParametersProvider implements Provider<Map<String, String[]>> {

    @SuppressWarnings("unchecked")
    public Map<String, String[]> get() {
        return IocContextManager.getRequest().getParameterMap();
    }
}
