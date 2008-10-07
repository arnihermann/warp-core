package com.wideplay.warp.widgets.client;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public interface Transport {
    <T> T in(InputStream in, Class<T> type) throws IOException;

    <T> InputStream out(Class<T> type, T data);
}
