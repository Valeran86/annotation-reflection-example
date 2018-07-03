package ru.sbt.examples.countMap;

import java.lang.reflect.Type;

public class Task3 {
    public static void main(String[] args) throws NoSuchFieldException {
        Type f = CountMapImpl.class.getField("countMapImpl").getGenericType();
    }
}
