package ru.sbt.examples.cacheproxy;

import java.util.*;

public class CacheManager<K, V> {

    private final HashMap<K,V> map;

    public CacheManager( ) {
        this.map = new HashMap<K, V>(  );
    }

    public final V get(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        V mapValue;

        mapValue = map.get(key);
        if (mapValue != null) {
            return mapValue;
        }

        V createdValue = create(key);
        if (createdValue == null) {
            return null;
        }

        if (!map.containsKey(key)) {
            // There was no conflict, create
            return put(key,createdValue);
        }
        else {
            return map.get(key);
        }
    }

    public final V put(K key, V value) {

        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }

        if (map.containsKey(key)) {
            return map.get(value);
        }

        map.put(key, value);
        V result = value;
        return result;

    }

    protected V create(K key) {
        return null;
    }
}
