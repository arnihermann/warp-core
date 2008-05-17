package com.wideplay.warp.widgets.binding;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public interface FlashCache {
    <T> T get(String key);

    <T> void put(String key, T t);
}
