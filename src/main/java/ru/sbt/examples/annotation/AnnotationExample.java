package ru.sbt.examples.annotation;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.math.BigDecimal;
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

    private static void checkColumnLimitation( Object object ) {
        // добавить проверки
        Class<?> clazz = object.getClass();

        for ( Field field : clazz.getDeclaredFields() ){

            if (field.isAnnotationPresent( Id.class )) {

                System.out.println("@Id is present");
                Id id = field.getAnnotation(Id.class);

                Object value = null;

                Boolean accessible = field.isAccessible();
                if (!accessible) field.setAccessible( true );
                try{
                    value = field.get( object );
                } catch ( Exception e){
                    e.printStackTrace();
                }finally {
                    field.setAccessible( accessible );
                }

                if ( (value == null) && (!field.isAnnotationPresent( GeneratedValue.class )) ) {
                    System.out.println("WARNING !!! @Id is present and NULL, OR @GeneratedValue is absent");
                    break;
                }

                System.out.println("Id is NULL: " + (value == null) +
                                    "; GeneratedValue is present: " +
                                    (field.isAnnotationPresent( GeneratedValue.class )));

                return;
            }

            System.out.println("@Id is absent");


        }
    }

    private static void checkPrimaryKey( Object object ) {

        Class<?> clazz = object.getClass();

        for ( Field field : clazz.getDeclaredFields() ){
            if (field.isAnnotationPresent( Column.class )) {
                Column column = field.getAnnotation( Column.class );
                int maxLength = column.length();

                Object value = null;

                Boolean accessible = field.isAccessible();

                if (!accessible) field.setAccessible( true );
                try {
                    value = field.get( object );
                } catch ( Exception e ){
                    e.printStackTrace();
                }finally {
                    field.setAccessible( accessible );
                }

                if (value == null) break;


                if (!isNullableCorrect( column, value )){
                    System.out.println("ERROR! ColumnName" + value + " is NOTNULL!");
                }

                if (!isColumnLengthCorrect( column, value )){
                    System.out.println("Error ColumnName:" + value);
                    System.out.println("WARNING!!! The length of the Column name must be no more than " + maxLength);
                }

                if ((value.getClass().getSuperclass().equals( java.lang.Number.class) && (!isPrecisionCorrect( column, value )))){
                    System.out.println("ERROR! Precission of " + value.toString() +  " must be " + column.precision());
                }

            }
        }
        
    }

    private static boolean isColumnLengthCorrect (Column column, Object value){
        if (value.toString().length() > column.length()) return false;
        return true;
    }

    private static boolean isNullableCorrect (Column column, Object value){
        if ((!column.nullable()) && (value.toString().length() == 0)) return false;
        return true;
    }

    private static boolean isPrecisionCorrect (Column column, Object value){
        int valuePrecision = (value.toString().length()-1) - value.toString().indexOf( '.', 1 );
        if (valuePrecision!=column.precision()) return false;
        return true;
    }
}
