package ru.sbt.examples.homework;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CountMapImpl <T> implements CountMap  <T>  {
    private HashMap <T, Integer>    map = new HashMap<T,Integer>(  );

    /**
     *  Метод возвращает тип класс, которым параметризирован T
     * */
    public Class classForT(T...t) {
        return t.getClass().getComponentType();
    }

    @Override
    public void add( T o ) {
        map.merge( o,1,Integer::sum );
    }

    @Override
    public int getCount( T o ) {
        Integer count =map.get( o ); // if value is null ?
        return count  == null ? 0 : count.intValue();
    }

    @Override
    public int remove( T o ) {
        Integer value = map.remove( o );
        return value == null ? 0 : value ;
    }

    @Override
    public int size() {
        return map.size();
    }



    @Override
    public void addAll( CountMap<T> source ) {
        Map <T, Integer > dest = source.toMap();
        for ( Map.Entry <T,Integer> value : dest.entrySet()       ) {
            map.merge(value.getKey(),value.getValue(), Integer::sum ) ;
        }
    }

    @Override
    public Map<T, Integer> toMap() {
        Map <T,Integer> result = new HashMap<>(  ) ;
        //result = map.clone() // не работает, почему не разобрался
        result.putAll( map );
        return result;
    }


    // если я правильно понял, то в destination уже может храниться карта с количеством, которую надо смержить
    @Override
    public void toMap(  Map <T ,Integer> destination ) {
        for ( Map.Entry <T,Integer> value : map.entrySet()       ) {
            destination.merge(value.getKey(),value.getValue(), Integer::sum ) ;
        }

    }
}
