package ru.sbt.examples.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class CountMapImpl {

    public MyCountMap<String> myCountMap = new MyCountMap<String>();

    public static void main( String[] args ) throws NoSuchFieldException {


        Class clazz = MyCountMap.class;
        Type genericFieldType = clazz.getGenericSuperclass();


        Field[] fields = clazz.getDeclaredFields();

        for(Field field : fields){
            System.out.println(field.getGenericType());
            System.out.println(field.toGenericString());
        }

        System.out.println(genericFieldType.toString());
        System.out.println(clazz.getSuperclass());
        

    }
}

