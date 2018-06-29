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
        Class<?> classes = object.getClass();
        for(Field field : classes.getDeclaredFields()) {
            boolean isSetAccessible=field.isAccessible();
            try {
                field.setAccessible(true);
                if(field.isAnnotationPresent(Id.class) &&
                        (!field.isAnnotationPresent(GeneratedValue.class) && field.get(object)==null)){
                    System.out.println("WARNING: ID=null и не имеет аннотацию GeneratedValue. "
                            + Objects.toString(object," [object is null]"));
                }
            } catch(IllegalAccessException e) {
                e.printStackTrace(System.out);
            }finally {
                field.setAccessible(isSetAccessible);
            }
        }
    }

    private static void checkPrimaryKey( Object object ) {
        Class<?> classes = object.getClass();
        for(Field field : classes.getDeclaredFields()) {
            boolean isSetAccessible=field.isAccessible();
            if(!field.isAnnotationPresent(Column.class)){
                continue;
            }
            try {
                field.setAccessible(true);
                Column annotationColumn=field.getAnnotation(Column.class);

                checkAnnotationColumnLength(field,annotationColumn,object);
                checkAnnotationColumnNullable(field,annotationColumn,object);
                checkAnnotationColumnPrecision(field,annotationColumn,object);

            }catch(IllegalAccessException e) {
                e.printStackTrace(System.out);
            }finally {
                field.setAccessible(isSetAccessible);
            }


        }
    }

    /** Проверка длинны значения показателя
     * @param field Поле с аннотацией Column
     * @param annotationColumn Аннонтация типа Column
     * @param object Объект содержащий поля c аннотацией Column
     * @throws IllegalAccessException
     */
    private static void checkAnnotationColumnLength(Field field, Column annotationColumn, Object object) throws IllegalAccessException{
        Object fieldValue=field.get(object);
        if(fieldValue!=null && (annotationColumn.length()<(fieldValue.toString()).length())){
            System.out.println("WARNING: Длинна значения \""+fieldValue+"\" превышает ограничение количества символов "
                    +(annotationColumn).length()+". "
                    + Objects.toString(object," [object is null]"));
        }
    }

    /** Проверка на null значение показаетеля
     * @param field Поле с аннотацией Column
     * @param annotationColumn Аннонтация типа Column
     * @param object Объект содержащий поля c аннотацией Column
     * @throws IllegalAccessException
     */
    private static void checkAnnotationColumnNullable(Field field, Column annotationColumn, Object object) throws IllegalAccessException {
        Object fieldValue=field.get(object);
        if(!(annotationColumn).nullable()&& (fieldValue==null)){
            System.out.println("WARNING: Значение атрибута "+field.getName()+" не должно быть null."
                    + Objects.toString(object," [object is null]"));
        }
    }


    /** Проверка на точность после запятой
     * precision имеется в виду как точность после запятой, а не по документации https://docs.oracle.com/javase/7/docs/api/java/math/BigDecimal.html#precision()
     * @param field Поле с аннотацией Column
     * @param annotationColumn Аннонтация типа Column
     * @param object Объект содержащий поля c аннотацией Column
     * @throws IllegalAccessException
     */
    private static void checkAnnotationColumnPrecision(Field field, Column annotationColumn, Object object) throws IllegalAccessException{
        Object fieldValue=field.get(object);

        if(fieldValue!=null && (annotationColumn.precision()>0) && (fieldValue instanceof Number)){
            String value=fieldValue.toString();
            if(value.indexOf(".")==-1 || value.split("[.]")[1].length()!=annotationColumn.precision()){
                System.out.println("WARNING: Точность \""+fieldValue+"\" не соответствует установленной точности "
                        + annotationColumn.precision() +". "
                        + Objects.toString(object," [object is null]"));
            };
        }
    }
}
