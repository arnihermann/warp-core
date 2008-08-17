package com.wideplay.warp.widgets.client;

import java.util.Map;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class WebResponse {
    private Map<String, String> headers;

    public Object to(Class<Json> transport) {
        return null;
    }

    public String plain() {
        return null;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public ResponseDataTypeBuilder from(Class<Json> transport) {
        return null;
    }
}
