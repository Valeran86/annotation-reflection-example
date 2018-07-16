package ru.sbt.examples.annotation;

import java.util.HashMap;
import java.util.Map;

public class CountMapIml <T> implements CountMap <T> {
    // добавляет элемент в этот контейнер.
    private Map<T, Integer> map = new HashMap<T, Integer>();
    public void add(T o) {
        int value=1;
        map.merge(o, value, Integer::sum);
    }

    //Возвращает количество добавлений данного элемента
    public int getCount(T o) {
        return map.get(o);
    }

    //Удаляет элемент из контейнера и возвращает количество его добавлений (до удаления)
    public int remove(T o) {
        int value = map.get(o);
        map.remove(o);
        return value;
    }

    //количество разных элементов
    public int size(){
        return map.size();
    }

    //Добавить все элементы из source в текущий контейнер, при совпадении ключей,     суммировать значения
    public void addAll(CountMap <T> source) {
        for (Map.Entry<T, Integer> pair: source.toMap().entrySet()) {
            map.merge(pair.getKey(), pair.getValue(), Integer::sum);
        }
    }

    //Вернуть java.util.Map. ключ - добавленный элемент, значение - количество его добавлений
    public Map<T, Integer> toMap() {
        Map<T, Integer>  map1 = new HashMap<T, Integer>(map);
        return map1;
    }

    //Тот же самый контракт как и toMap(), только всю информацию записать в destination
    public void toMap(Map<T, Integer> destination) {
        for (Map.Entry<T, Integer> pair: map.entrySet()){
            destination.merge(pair.getKey(), pair.getValue(), Integer::sum);
        }


    }

}