package ru.sbt.examples.annotation;

import java.util.HashMap;
import java.util.Map;

public class MyCountMap <E> implements CountMap<E>{

    Map<E, Integer> container = new HashMap<E, Integer>();

    // добавляет элемент в этот контейнер.
    public void add(E o) {
        if (!container.containsKey(o)){
            container.put(o, new Integer(0));
        }
        container.put(o, container.get(o) + 1);
    }

    //Возвращает количество добавлений данного элемента
    public int getCount(E o) {
        if (!container.containsKey(o)) return 0;
        return container.get(o);
    }

    //Удаляет элемент из контейнера и возвращает количество его добавлений (до удаления)
    public int remove(E o) {
        int count = container.get(o);
        container.remove(o);
        return count;
    }

    //количество разных элементов
    public int size() {
        return container.size();
    }

    //Добавить все элементы из source в текущий контейнер, при совпадении ключей, суммировать значения
    public void addAll(CountMap source) {
        Map tmp = source.toMap();
        container.putAll(tmp);
    }

    //Вернуть java.util.Map. ключ - добавленный элемент, значение - количество его добавлений
    public Map toMap() {
        Map<E, Integer> newContainer = new HashMap<E, Integer>(container);
        return newContainer;
    }

    //Тот же самый контракт как и toMap(), только всю информацию записать в destination
    public void toMap(Map destination) {
        destination.putAll(container);
    }

    @Override
    public String toString() {
        return container.toString();
    }
}