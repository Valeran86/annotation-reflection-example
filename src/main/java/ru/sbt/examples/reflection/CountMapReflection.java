package ru.sbt.examples.reflection;

import ru.sbt.examples.collection.CountMapImpl;

import java.lang.reflect.*;

//Внешний класс по отношению к CountMapImpl
public class CountMapReflection {
    static CountMapImpl<Integer> map = new CountMapImpl<>();

    public static void main( String[] args )  {

        TypeVariable[] typeVariables= CountMapImpl.class.getTypeParameters();
        for(TypeVariable typeVariable:typeVariables){
            System.out.println("Параметр инициализации при обращении к классу:"+typeVariable.getName());
        }

        typeVariables= map.getClass().getTypeParameters();
        for(TypeVariable typeVariable:typeVariables){
            System.out.println("Параметр инициализации при обращении к объекту класса:"+typeVariable.getName());
        }

        typeVariables= map.reflectionResult();
        for(TypeVariable typeVariable:typeVariables){
            System.out.println("Параметр инициализации непосредственно в объекте:"+typeVariable.getName());
        }

        System.out.println("Параметр инициализации атрибута myMap в самом объекте: " + map.reflectionResultMyMap());

        try {
            Field field=CountMapReflection.class.getDeclaredField("map");
            field.setAccessible(true);
            Type genericFieldType = field.getGenericType();
            if(genericFieldType instanceof ParameterizedType){
                ParameterizedType parameterizedType = (ParameterizedType) genericFieldType;
                Type[]  actualTypeArguments= parameterizedType.getActualTypeArguments();
                for(Type type : actualTypeArguments){
                    Class typeClass = (Class) type;
                    System.out.println("Параметр инициализации атрибута на основе класса: " + typeClass);
                }
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }
}
