package ru.sbt.examples.countmap;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class CountMapOuter {
    public static void main( String[] args ) throws NoSuchFieldException {
        Class clazz = CountMapInner.class;
        Field field = clazz.getDeclaredField( "cmi" );
        Type cmiFieldGenericType = field.getGenericType();
        if ( ParameterizedType.class.isAssignableFrom( cmiFieldGenericType.getClass() ) ) {
            ParameterizedType parameterizedType = (ParameterizedType) cmiFieldGenericType;
            for ( Type type : parameterizedType.getActualTypeArguments() ) {
                Class classType = (Class) type;
                System.out.format( "Field '%s' of class '%s' have param T : %s",
                        field.getName(),
                        clazz.getName(),
                        classType.getName() );
            }
        }
    }
}
