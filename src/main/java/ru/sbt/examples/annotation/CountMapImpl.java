package ru.sbt.examples.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class CountMapImpl {

    public MyCountMap<String> myCountMap = new MyCountMap<String>();

    public static void main( String[] args ) throws NoSuchFieldException {
        Field field = CountMapImpl.class.getField("myCountMap");

        Class clazz = MyCountMap.class;
        Type genericFieldType = clazz.getGenericSuperclass();

        System.out.println(genericFieldType.toString());

    }
}

