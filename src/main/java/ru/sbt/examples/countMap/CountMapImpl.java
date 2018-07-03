package ru.sbt.examples.countMap;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class CountMapImpl {
    public static final CountMap<Integer> countMapImpl = new CountMap<>();

    public static void main(String[] args) throws NoSuchFieldException {
        Field f = CountMapImpl.class.getField("countMapImpl");
        Type t = f.getGenericType();
    }
}
