package ru.sbt.examples.reflection;

import ru.sbt.examples.collection.CountMapImpl;

import java.lang.reflect.*;

//Внешний класс по отношению к CountMapImpl
public class CountMapReflection {
    static CountMapImpl<Integer> map = new CountMapImpl<>();

    public static void main( String[] args )  {

        try {
            Field field=CountMapReflection.class.getDeclaredField("map");
            field.setAccessible(true);
            Type genericFieldType = field.getGenericType();
            if(genericFieldType instanceof ParameterizedType){
                ParameterizedType parameterizedType = (ParameterizedType) genericFieldType;
                Type[]  actualTypeArguments= parameterizedType.getActualTypeArguments();
                for(Type type : actualTypeArguments){
                    Class typeClass = (Class) type;
                    System.out.println("Параметр инициализации атрибута при создании объекта: " + typeClass);
                }
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace(System.out);
        }
    }
}
