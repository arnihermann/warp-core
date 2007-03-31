package com.wideplay.warp.util;

import java.util.Map;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * On: 20/03/2007
 *
 * A HashMap-backed impl of a Cube. A cube is generally twice as expensive for every operation
 * of a Map analog (but equal to a map of maps, which is expected). Sometimes it can be more expensive
 * on put() if a hash miss occurs on K2 and a target map has to be created to store the value.
 *
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class HashCube<K1, K2, V> implements Cube<K1, K2, V> {
    public final Map<K1, Map<K2, V>> keyMap = new HashMap<K1, Map<K2, V>>();

    public void put(K1 key1, K2 key2, V value) {
        Map<K2, V> valueMap = keyMap.get(key1);

        //if there is no value map, add one lazily
        if (null == valueMap) {
            valueMap = new HashMap<K2, V>();
            keyMap.put(key1, valueMap);
        }

        valueMap.put(key2, value);
    }

    public V get(K1 key1, K2 key2) {
        Map<K2, V> valueMap = keyMap.get(key1);

        return (null == valueMap) ? null : valueMap.get(key2);
    }

    public void clear() {
        keyMap.clear();
    }
}
