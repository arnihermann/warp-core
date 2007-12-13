package com.wideplay.warp.util;

/**
 * Created with IntelliJ IDEA.
 * On: 20/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
public interface Cube<K1, K2, V> {
    void put(K1 key1, K2 key2, V value);

    V get(K1 key1, K2 key2);

    void clear();
}
