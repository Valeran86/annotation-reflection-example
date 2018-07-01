package ru.sbt.examples.annotation;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * II. Аннотации
 * Проверить экземпляр класса на соответствие ограничениям, заданным в аннотации:
 * <p>
 * 1. Если есть колонка помеченная как @Id, то либо значение поля не null, либо также есть аннотация @GeneratedValue
 *
 * @see javax.persistence.Id
 * @see javax.persistence.GeneratedValue
 * <p>
 * 2. Проверить что значение в колонке, отмеченной аннотацией @Column соответсвует заданным ограничениям
 * @see javax.persistence.Column
 */
public class AnnotationExample {

    public static void main( String... args ) {
        List< Object > ormExampleList = Arrays.asList(
                ORMExample.builder( )
                        .name( "вот такое имя хочу сохранить в БД" )
                        .active( null )
                        .build( )
                , ORMExample.builder( )
                        .name( "shorter name" )
                        .active( Boolean.TRUE )
                        .build( )
                , ORMExample2.builder( )
                        .id( 1 )
                        .service( "очень-очень длинная строка очень-очень длинная строка очень-очень длинная строка очень-очень длинная строка очень-очень длинная строка очень-очень длинная строка очень-очень длинная строка " )
                        .price( 123.45 )
                        .build( )
                , ORMExample2.builder( )
                        .service( "разработка на java" )
                        .price( 123456789.123456789 )
                        .build( )
        );
        System.out.println( "ormExampleList\n" + ormExampleList );

        for ( Object ormExample : ormExampleList ) {
            //
            checkPrimaryKey( ormExample );
            //
            checkColumnLimitation( ormExample );
        }
    }

    private static void checkColumnLimitation( Object obj ) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field: fields) {
            if ( field.isAnnotationPresent( Column.class ) ) {
                Column col = field.getAnnotation( Column.class );
                Object value = null;
                field.setAccessible( true );

                try {
                    value = field.get( obj );
                    if (value!=null&&value.toString( ).equals( col.name( ) )&&value.toString().length()==col.length() )
                    //(col.nullable()==(value==null)))
                    //&&value.toString().length()==col.length()
                    {
                        System.out.println( "Значение " + value + " соответсвует заданным ограничениям" );

                    } else {
                        System.out.println( "Значение " +  value + " не соответсвует заданным ограничениям" );
                    }

                } catch ( IllegalAccessException e ) {
                    e.printStackTrace( );
                }


            }
        }
        // добавить проверки
    }

    private static void checkPrimaryKey( Object object ) {

        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field: fields) {
            if ( !field.isAnnotationPresent( Id.class ) )
                continue;

            if ( field.isAnnotationPresent( GeneratedValue.class ) )
                continue;
            field.setAccessible( true );
            Object value = null;
            try {
                value = field.get( object );
            } catch ( IllegalAccessException e ) {
                e.printStackTrace( );
            }
            if ( value == null )
                System.out.println( "Id null" + object.toString( ) );
        }
    }
}