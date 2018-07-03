package ru.sbt.examples.countMap;

import java.util.HashMap;
import java.util.Map;

public class CountMap<T> implements ICountMap<T>{
    private Map<T, Integer> map = new HashMap<>();
    public void add(T o) {
        map.merge(o, 1, (oldValue, newValue) -> oldValue + newValue);
    }

    public int getCount(T o) {
        Integer temp = map.get(o);
        return temp == null? -1: temp;
    }

    public int remove(T o) {
        Integer temp = map.remove(o);
        return temp == null? -1: temp;
    }

    public int size() {
        return map.size();
    }

    public void addAll(CountMap<T> source) {
        for ( Map.Entry<T, Integer> entry : source.map.entrySet() ) {
            map.merge(entry.getKey(), entry.getValue(), (oldValue, newValue) -> oldValue + newValue);
        }
    }

    public Map<T, Integer> toMap() {
        return map;
    }

    public void toMap(Map<T, Integer> destination) {
        destination.clear();
        destination.putAll(map);
    }

    @Override
    public String toString() {
        return map.toString();
    }
}