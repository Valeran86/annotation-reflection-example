package ru.sbt.examples.annotation;

import javax.persistence.*;
import java.lang.annotation.*;
import java.lang.reflect.Field;
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

    public static void main( String... args ) throws IllegalAccessException {
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
                        //.id( 1 )
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

    private static void checkColumnLimitation( Object object ) throws IllegalAccessException {
        Class<?> clazz = object.getClass();
        boolean acc;
        Column column;
        int length;
        String name;
        boolean nullable;
        int precision;
        for(Field field : clazz.getDeclaredFields()){
            if(!field.isAnnotationPresent(Column.class)) continue;
            column = field.getAnnotation(Column.class);
            length = column.length();
            nullable = column.nullable();
            precision = column.precision();

            acc = field.isAccessible();
            field.setAccessible(true);
            Object value = field.get(object);
            if(value==null && nullable == false) System.out.println("В объекте " + object.toString() +
                " поле " + field.getName() + " нарушает аннотацию Column nullable = false");
            if(Number.class.isAssignableFrom(field.getType())){
                if(!((Double) value).toString().matches("\\d*\\.\\d{0," + precision +"}")) {
                    System.out.println("В объекте " + object.toString() +
                            " поле " + field.getName() + " нарушает аннотацию Column precision = " + precision);
                }
            }
            if(field.getType() == String.class){
                if(value.toString().length()>length) {
                    System.out.println("В объекте " + object.toString() +
                            " поле " + field.getName() + " нарушает аннотацию Column length = " + length);
                }
            }

            field.setAccessible(acc);
        }
    }

    private static void checkPrimaryKey( Object object ) throws IllegalAccessException {
        Class<?> clazz = object.getClass();
        boolean acc;
        for(Field field : clazz.getDeclaredFields()){
            //System.out.println("Поле: " + field.getName());
            if(!field.isAnnotationPresent(Id.class)) continue;
            //System.out.println("Это поле ID");
            if(field.isAnnotationPresent(GeneratedValue.class)) continue;
            //System.out.println("Это геренрируемое поле");
            acc = field.isAccessible();
            field.setAccessible(true);
            if (field.get(object)==null){
                System.out.println("Поле ID пустое для объекта " + object.toString());
            }
            field.setAccessible(acc);
        }
    }
}
