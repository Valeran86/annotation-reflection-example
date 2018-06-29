package ru.sbt.examples.collection;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

public class CountMapImpl<T> implements CountMap<T> {
    private Map<T,Integer> myMap=new HashMap<>();

    @Override
    public void add(T o) {
        myMap.merge(o,1,Integer::sum);
    }

    @Override
    public int getCount(T o) {
        return myMap.getOrDefault(o, 0);
    }

    @Override
    public int remove(T o) {
        Integer countObj=myMap.get(o);
        if(countObj<2){
            myMap.remove(o);
        }else{
            myMap.put(o,countObj--);
        }
        return countObj;
    }

    @Override
    public int size() {
        return myMap.size();
    }

    @Override
    public void addAll(CountMap<T> source) {
        for(Map.Entry<T, Integer> entry:source.toMap().entrySet()){
            if(myMap.containsKey(entry.getKey())){
                myMap.put(entry.getKey(), myMap.get(entry.getKey())+entry.getValue());
            }else{
                myMap.put(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public Map<T, Integer> toMap() {
        return new HashMap<>(myMap);
    }

    @Override
    public void toMap(Map<T, Integer> destination) {
        destination.putAll(myMap);
    }
}
