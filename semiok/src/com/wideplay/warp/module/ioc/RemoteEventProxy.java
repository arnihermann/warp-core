package com.wideplay.warp.module.ioc;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * On: 28/08/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
public interface RemoteEventProxy {
    String WARP_TARGET_PAGE_URI = "__warpTargetPageUri";

    String dispatchEvent(Map<String, String> params);
}
