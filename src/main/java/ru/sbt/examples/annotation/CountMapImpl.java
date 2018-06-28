package ru.sbt.examples.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class CountMapImpl {

    public MyCountMap<String> myCountMap = new MyCountMap<String>();

    public static void main( String[] args ) throws NoSuchFieldException {
        Field field = CountMapImpl.class.getField("myCountMap");

        Type genericFieldType = field.getGenericType();

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

