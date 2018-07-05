package ru.sbt.examples.homework;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 3) Создать экземпляр класса CountMapImpl,
 * проинициализировать его и затем попытаться достать,
 * каким типом инициализирован параметр класса из внешнего класса
 * и в методе, внутри объекта.
 *
 *
 *
 * */
public class testCountMap {


    public static void main( String[] args ) throws NoSuchFieldException  {
        CountMapImpl<String> countMap  = new CountMapImpl<>();

        System.out.println(countMap.getClass().getDeclaredField("map").getGenericType()); //java.util.HashMap<T, java.lang.Integer>

        // Что бы получить тип класса <T>,   необходимо определять тип в самом классе  CountMapImpl
        // Добавил один метод classForT в CountMapImpl

        // источник:   https://habr.com/post/66593/
        // http://qaru.site/questions/15716/get-generic-type-of-class-at-runtime
        System.out.println(countMap.classForT(  )); //class java.lang.String




    }



}
