package com.wideplay.warp.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * On: 20/03/2007
 *
 * A HashMap-backed impl of a Cube. This cube is fast as I can make it, uses a tuple as a 2-pair key.
 *
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class HashCube<K1, K2, V> implements Cube<K1, K2, V> {
    private final Map<KeyTuple, V> keyMap;


    public HashCube() {
        this(new HashMap<KeyTuple, V>());
    }

    protected HashCube(Map<KeyTuple, V> keyMap) {
        this.keyMap = new HashMap<KeyTuple, V>();
    }

    private class KeyTuple {
        private final K1 key1;
        private final K2 key2;

        private Integer hashCode;

        public KeyTuple(K1 key1, K2 key2) {
            this.key1 = key1;
            this.key2 = key2;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            KeyTuple keyTuple = (KeyTuple) o;

            if (key1 != null ? !key1.equals(keyTuple.key1) : keyTuple.key1 != null) return false;
            if (key2 != null ? !key2.equals(keyTuple.key2) : keyTuple.key2 != null) return false;

            return true;
        }

        public int hashCode() {
            //memoize hashcode for performance
            if (null != hashCode)
                return hashCode;

            //else
            hashCode = (key1 != null ? key1.hashCode() : 0);
            hashCode = 31 * hashCode + (key2 != null ? key2.hashCode() : 0);
            return hashCode;
        }
    }

    public void put(K1 key1, K2 key2, V value) {
        //note: destructive!!
        keyMap.put(new KeyTuple(key1, key2), value);
    }

    public V get(K1 key1, K2 key2) {
        return keyMap.get(new KeyTuple(key1, key2));
    }

    public void clear() {
        keyMap.clear();
    }
}
