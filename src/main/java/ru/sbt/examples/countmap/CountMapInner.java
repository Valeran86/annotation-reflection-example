package ru.sbt.examples.countmap;

import ru.sbt.collections.CountMapImpl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class CountMapInner {
    private CountMapImpl<String> cmi = new CountMapImpl<>();

    public static void main( String[] args ) throws NoSuchFieldException {
        Field field = CountMapInner.class.getDeclaredField( "cmi" );
        Type cmiFieldGenericType = field.getGenericType();
        if ( ParameterizedType.class.isAssignableFrom( cmiFieldGenericType.getClass() ) ) {
            ParameterizedType parameterizedType = (ParameterizedType) cmiFieldGenericType;
            for ( Type type : parameterizedType.getActualTypeArguments() ) {
                Class classType = (Class) type;
                System.out.format( "Own field '%s' have param T : %s", field.getName(), classType.getName() );
            }
        }
    }
}