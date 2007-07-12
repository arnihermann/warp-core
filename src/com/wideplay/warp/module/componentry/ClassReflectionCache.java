package com.wideplay.warp.module.componentry;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: dprasanna
 * Date: 26/03/2007
 * Time: 16:33:13
 *
 * An interface that caches reflection (introspection) of frequently reflected classes.
 *
 * @author dprasanna
 * @since 1.0
 */
public interface ClassReflectionCache {
    Map<String, String> getPropertyLabelMap(Object object);
}
