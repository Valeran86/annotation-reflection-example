package ru.sbt.examples.annotation;

import java.lang.reflect.InvocationTargetException;

public class TestBeanUtils {
    public static void main( String[] args ) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        System.out.println("Class fromClass.value");
        FromClass fromClass = new FromClass( 10 );
        System.out.println(fromClass.getValue());

        System.out.println("Class toClass.value");
        ToClass toClass = new ToClass();
        System.out.println(toClass.toString());

        System.out.println("assign....");
        BeanUtils.assign( toClass, fromClass );

        System.out.println("Class toClass.value:");
        System.out.println(toClass.toString());

    }
}
