package com.wideplay.warp.internal;

import com.wideplay.warp.module.WarpConfiguration;
import com.google.inject.Provider;

/**
 * Created by IntelliJ IDEA.
 * User: dhanji
 * Date: Aug 13, 2007
 * Time: 10:08:18 AM
 */
class WarpConfigurationProvider implements Provider<WarpConfiguration> {
    private final WarpConfiguration configuration;

    public WarpConfigurationProvider(WarpConfiguration configuration) {
        this.configuration = configuration;
    }

    public WarpConfiguration get() {
        return configuration;
    }
}
