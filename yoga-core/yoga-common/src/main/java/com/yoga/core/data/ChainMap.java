package com.yoga.core.data;

import java.util.HashMap;

public class ChainMap<K, V> extends HashMap<K, V> {
    private static final long serialVersionUID = -9148459395437048765L;

    public ChainMap<K, V> set(K key, V value) {
        if (value == null)return this;
        super.put(key, value);
        return this;
    }
}
