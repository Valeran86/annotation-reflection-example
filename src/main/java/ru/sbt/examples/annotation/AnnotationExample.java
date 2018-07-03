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
        List<Object> ormExampleList = Arrays.asList(
                ORMExample.builder()
                        .name( "вот такое имя хочу сохранить в БД" )
                        .active( null )
                        .build()
                , ORMExample.builder()
                        .name( "shorter name" )
                        .active( Boolean.TRUE )
                        .build()
                , ORMExample2.builder()
                        .id( 1 )
                        .service( "очень-очень длинная строка очень-очень длинная строка очень-очень длинная строка очень-очень длинная строка очень-очень длинная строка очень-очень длинная строка очень-очень длинная строка " )
                        .price( 123.45 )
                        .build()
                , ORMExample2.builder()
                        .id( null )
                        .service( "разработка на java" )
                        .price( 123456789.123456789 )
                        .build()
        );
        System.out.println( "ormExampleList\n" + ormExampleList );

        for ( Object ormExample : ormExampleList ) {
            // 1
            checkPrimaryKey( ormExample );
            // 2
            checkColumnLimitation( ormExample );
        }
    }

    private static boolean isAnnotationNullableCorrect( Object fieldValue, Column annotation ) {
        boolean ret = true;
        if ( !annotation.nullable() )
            ret = fieldValue != null;
        return ret;
    }

    private static boolean isAnnotationLengthCorrect( Object fieldValue, Column annotation ) {
        boolean ret = true;
        if ( fieldValue instanceof String ) {
            ret = ( (String) fieldValue ).length() <= annotation.length();
        }
        return ret;
    }

    private static boolean isAnnotationPrecisionCorrect( Object fieldValue, Column annotation ) {
        boolean ret = true;
        if ( fieldValue instanceof Float || fieldValue instanceof Double ) {
            String strValue = String.valueOf( fieldValue );
            ret = strValue.contains( "." ) && strValue.split( "\\." )[1].length() <= annotation.precision();
        }
        return ret;
    }

    private static void checkColumnLimitation( Object object ) {
        // добавить проверки
        //2. Проверить что значение в колонке, отмеченной аннотацией @Column соответсвует заданным ограничениям
        for ( Field field : object.getClass().getDeclaredFields() ) {
            if ( !field.isAnnotationPresent( Column.class ) ) continue;
            Column columnAnnotation = field.getAnnotation( Column.class );
            Boolean isAccessible = field.isAccessible();
            try {
                field.setAccessible( true );
                Object fieldValue = field.get( object );
                // если какой то из проверок - false -> true
                if ( !( isAnnotationNullableCorrect( fieldValue, columnAnnotation )
                        & isAnnotationLengthCorrect( fieldValue, columnAnnotation )
                        & isAnnotationPrecisionCorrect( fieldValue, columnAnnotation ) ) )
                    System.out.format( "Object \n\t[%s]\nValue of the field [%s] \"%s\" is incorrect!\n",
                            Objects.toString( object, "object is null" ),
                            field.getName(),
                            fieldValue );
            } catch ( IllegalAccessException e ) {
                e.printStackTrace( System.out );
            } finally {
                field.setAccessible( isAccessible );
            }
        }
    }

    private static void checkPrimaryKey( Object object ) {
        // добавить проверки
        //1. Если есть колонка помеченная как @Id, то либо значение поля не null,
        //   либо также есть аннотация @GeneratedValue
        Boolean primaryIsOK = true;
        // прохожу все поля потому, что PK может быть составным
        for ( Field field : object.getClass().getDeclaredFields() ) {
            Boolean isAccessible = field.isAccessible();
            try {
                field.setAccessible( true );
                if ( field.isAnnotationPresent( Id.class ) )
                    primaryIsOK &= field.isAnnotationPresent( GeneratedValue.class ) || field.get( object ) != null;
            } catch ( IllegalAccessException e ) {
                e.printStackTrace( System.out );
            } finally {
                field.setAccessible( isAccessible );
            }
        }
        System.out.format( "Object %s \n\tcontains %s PK", object, ( primaryIsOK ? "correct" : "incorrect" ) );
    }
}


