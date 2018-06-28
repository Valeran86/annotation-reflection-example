package ru.sbt.examples.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

//Класс внешний по отношению к CountMapReflection, но я считаю что это не то что имелось в виду в задании.
public class ExternalClassCountMapReflection {

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
                    System.out.println("Внешний класс: Параметр инициализации атрибута на основе класса: " + typeClass);
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
