package ru.sbt.examples.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class RealizeBeanUtils {
    public static void main(String [] args){
        TestClassFrom testObjectFrom = new TestClassFrom ();
        testObjectFrom.name="Название";
        testObjectFrom.value = 12;
        TestClassTo testObjectTo = new TestClassTo();
        BeanUtils.assign( testObjectFrom, testObjectTo );
        System.out.println("Значения полей в testObjectTo: " +  testObjectTo.name + " " + testObjectTo.value);
    }

}