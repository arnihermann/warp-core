package com.wideplay.warp.widgets.client;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class Json implements Transport {

    public <T> T in(InputStream in, Class<T> type) throws IOException {
        throw new UnsupportedOperationException();
    }

    public <T> InputStream out(Class<T> type, T data) {
        throw new UnsupportedOperationException();
    }
}
