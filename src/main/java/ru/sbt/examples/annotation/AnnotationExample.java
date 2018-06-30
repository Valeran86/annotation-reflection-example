package ru.sbt.examples.annotation;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.lang.reflect.AnnotatedType;
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
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                Object value = new Object();
                try {
                    field.setAccessible(true);
                    value = field.get(object);
                }
                catch (IllegalAccessException e) {
                    System.out.println(e.getMessage());
                }
                if (column.precision() != 0) {
                    if (value.toString() != String.format("%.2f", value)) {
                        System.out.println("Объект " + object.toString() + " имеет неверную точность");
                    }
                }
                if (!column.nullable()) {
                    if (value == null) {
                        System.out.println("Объект " + object.toString() + " имеет null в " + field.toString());
                    }
                }
                if (column.length() != 255) {
                    if (value.toString().length() > 30) {
                        System.out.println("Объект " + object.toString() + " имеет неверное имя");
                    }
                }
            }
        }
    }


    private static void checkPrimaryKey( Object object ) {
        for (Field field : object.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Id.class) && !field.isAnnotationPresent(GeneratedValue.class)&& field.get(object) == null) {
                    System.out.println("Объект " + object.toString() + " имеет null в поле Id");
                }
            }
            catch (IllegalAccessException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
