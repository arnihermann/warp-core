package com.wideplay.warp.widgets.binding;

import com.google.inject.ImplementedBy;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@ImplementedBy(ConcurrentPropertyCache.class)
public interface PropertyCache {
    boolean exists(String property, Class<?> anObjectClass);
}
