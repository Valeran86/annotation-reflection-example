package ru.sbt.examples.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class CountMapImpl {

    public MyCountMap<String> myCountMap = new MyCountMap<String>();

    public static void main( String[] args ) throws NoSuchFieldException {


        Class clazz = MyCountMap.class;
        Type genericFieldType = clazz.getGenericSuperclass();

        System.out.println(genericFieldType.toString());
        System.out.println(clazz.getSuperclass());

        Field[] fields = clazz.getDeclaredFields();

        System.out.println();

        for(Field field : fields){
            System.out.println(field.getGenericType());
            System.out.println(field.toGenericString());

            genericFieldType = field.getGenericType();

            if(genericFieldType instanceof ParameterizedType){
                ParameterizedType aType = (ParameterizedType) genericFieldType;
                Type[] fieldArgTypes = aType.getActualTypeArguments();
                for(Type fieldArgType : fieldArgTypes){
                    Class fieldArgClass = (Class) fieldArgType;
                    System.out.println("fieldArgClass = " + fieldArgClass);
                }
            }

        }



    }
}

