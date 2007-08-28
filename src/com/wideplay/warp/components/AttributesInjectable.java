package com.wideplay.warp.components;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * On: 20/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public interface AttributesInjectable {
    void setAttributeNameValuePairs(Map<String, Object> attribs);

    Map<String, Object> getAttributeNameValuePairs();
}
