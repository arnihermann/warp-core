package com.wideplay.warp.module.components;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: dprasanna
 * Date: 26/03/2007
 * Time: 16:33:13
 * <p/>
 * TODO: Describe me!
 *
 * @author dprasanna
 * @since 1.0
 */
public interface ClassReflectionCache {
    Map<String, String> getPropertyLabelMap(Object object);
}
