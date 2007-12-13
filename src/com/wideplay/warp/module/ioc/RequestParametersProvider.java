package com.wideplay.warp.module.ioc;

import com.google.inject.Provider;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * On: 25/03/2007
 *
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
class RequestParametersProvider implements Provider<Map<String, String[]>> {

    @SuppressWarnings("unchecked")
    public Map<String, String[]> get() {
        return IocContextManager.getRequest().getParameterMap();
    }
}
