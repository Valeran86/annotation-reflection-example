package ru.sbt.examples.annotation;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                        .id( null )
                        .name( "вот такое имя хочу сохранить в БД" )
                        .active( null )
                        .build()
                , ORMExample.builder()
                        .id( null )
                        .name( "shorter name" )
                        .active( Boolean.TRUE )
                        .build()
                , ORMExample2.builder()
                        .id( null )
                        .service( "очень-очень длинная строка очень-очень длинная строка очень-очень длинная строка очень-очень длинная строка очень-очень длинная строка очень-очень длинная строка очень-очень длинная строка " )
                        .price( 123.45 )
                        .build()
                , ORMExample2.builder()
                        .id( 1 )
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

    private static void checkColumnLimitation( Object object ) {
        // добавить проверки
        Class<?> cls = object.getClass();
        if ( cls == null )
            return;
        for ( Field field : cls.getDeclaredFields() ) {
            field.setAccessible( true );
            try {
                if ( field.isAnnotationPresent( Id.class ) ) {
                    if ( field.get( object ) != null )
                        continue;
                    if ( field.get( object ) == null && field.isAnnotationPresent( GeneratedValue.class ) )
                        continue;
                    System.out.println( "Ошибка проверки класса " + cls.getName() + ", поля: " + field.getName() );
                }
            } catch ( IllegalAccessException e ) {
                System.out.println( e.getMessage() );
            }

        }
    }

    // Проверить что значение в колонке, отмеченной аннотацией @Column соответсвует заданным ограничениям
    private static void checkPrimaryKey( Object object ) {
        // добавить проверки
        Class<?> cls = object.getClass();
        if ( cls == null )
            return;
        for ( Field field : cls.getDeclaredFields() ) {
            field.setAccessible( true );
            Object value = null;
            if ( field.isAnnotationPresent( Column.class ) ) {
                Annotation ann = field.getAnnotation( Column.class );
                try {
                    value = field.get( object );
                    //System.out.println( field.getName() +"="+ value );
                } catch ( IllegalAccessException e ) {
                    System.out.println( e.getMessage() );
                }


                Class<? extends Annotation> type = ann.annotationType();
                //System.out.println( "Значение поля: " + value );
                // Перебираем методы(атрибцты) аннтоации
                for ( Method method : type.getDeclaredMethods() ) {
                    Object attrValue = null;
                    try {
                        attrValue = method.invoke( ann, new Object[ 0 ] );
                    } catch ( IllegalAccessException e ) {
                        System.out.println( e.getMessage() );
                    } catch ( InvocationTargetException e ) {
                        System.out.println( e.getMessage() );
                    }
                    //System.out.println( " " + method.getName() + " = " + attrValue );
                    // @Column( length = 30 )
                    if ( method.getName().equals( "length" ) && value != null ) {
                        if ( value.toString().length() > ( Integer ) attrValue ) {
                            System.out.println( "=====================================================" );
                            System.out.println( "Ошибка! " + method.getName() + " от строки '" + value + "' больше " + ( Integer ) attrValue );
                        }
                    }
                    // @Column( nullable = false )
                    if ( method.getName().equals( "nullable" ) ) {
                        if ( value == null && ( Boolean ) attrValue == false ) {
                            System.out.println( "=====================================================" );
                            System.out.println( "Ошибка! " + method.getName() + "=" + ( Boolean ) attrValue + " для поля " + field.getName() + "=" + value + " ! " );
                        }
                    }
                    // @Column( precision = 2 )
                    if ( method.getName().equals( "precision" ) ) {
                        if ( value != null && value instanceof Number ) {
                            String prec = value.toString();
                            if ( value.toString().substring( prec.indexOf( "." ) + 1 ).length() != 2 ) {
                                System.out.println( "=====================================================" );
                                System.out.println( "Ошибка! " + method.getName() + "!=" + attrValue + " для поля " + field.getName() + "=" + value + " ! " );
                            }
                        }
                    }
                }
            }
        }
    }
}
