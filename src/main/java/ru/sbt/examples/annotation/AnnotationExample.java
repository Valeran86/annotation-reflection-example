package ru.sbt.examples.annotation;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
    // https://habr.com/post/309222/
    // mvn exec:java -Dexec.mainClass="ru.sbt.examples.annotation.AnnotationExample"
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
                , ORMExample2.builder()
                        .service( "ID is NULL" )
                        .price( 123d )
                        .build()
        );
        System.out.println( "ormExampleList\n" + ormExampleList );

        for ( Object ormExample : ormExampleList ) {
            try {
                // 1
                //checkPrimaryKey( ormExample );
                // 2
                checkColumnLimitation( ormExample );
            }
            catch (  java.lang.IllegalAccessException ex  ){
                ex.printStackTrace(System.out);
            }

        }
    }

    private static void checkColumnLimitation( Object object )  throws IllegalAccessException  {
        // добавить проверки
        Class <?> clazz = object.getClass();
        for (Field field:  clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if  (!( field.isAnnotationPresent(Column.class  ) ))
                continue;

            Object value = field.get( object );
            Column  annotation = field.getAnnotation( Column.class );
            Class<? extends Annotation> type = annotation.annotationType();
                // NULL
            if (!(checkColumnNull (value,annotation))){
                System.out.println( "Value Can't be NULL !  " + clazz.getSimpleName() + "." + field.getName() + " " + object.toString() );
                break; // дальше это поле  проверять не имеет смысла
            }
            // Дальше проверки идут специфические для :
            //  1. строк
            //  2. Number Precision
            //
            switch ( getNameTypeValue(value) ){
                case "String":{
                    if (!(checkColumnLength (value.toString(),annotation)))
                        System.out.println( "Превышение длины строки len = "+value.toString().length()+"+!  " + clazz.getSimpleName() + "." + field.getName() +" = " +object.toString() );
                    break;
                }
                case "Double":
                case "Float" : {
                    if (!(checkColumnPrecision ( (Number) value ,annotation))){
                        System.out.println( "Не выполняется условие Precision !  " + clazz.getSimpleName() + "." + field.getName() +" = " +object.toString() );
                    }
                    break;
                }
                default: {
                }
            }
        }
    }

    private static String getNameTypeValue (Object value){
        if (value instanceof String )
            return "String";
        if (value instanceof Float)
            return "Float";
        if (value instanceof Double)
            return "Double";
        return "someObject";
    }

    /** Проверка на NULL
     * true = все хорошо, false проверка на null не пройдена
     */
    private static boolean checkColumnNull (Object value, Column  annotation  ){
        return   annotation.nullable() ? true : value !=null ;
    }

    private static boolean checkColumnLength (String value, Column  annotation  ){
        return    value.length( ) <=  annotation.length()  ;
    }

    private static <T extends Number> boolean checkColumnPrecision (T  value, Column  annotation  ){
        return value.toString().split("\\.")[1].length() <= annotation.precision();
    }

    /**
     * only for ORMExample2
     */
    private static  void checkPrimaryKey( Object  object ) throws IllegalAccessException {
        // добавить проверки : NOT NULL  : если value is NULL , тогда значение должно генерироваться
        Class <?  > clazz = object.getClass();
        for (Field field:  clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if ( !field.isAnnotationPresent( javax.persistence.Id.class ) )
                continue;
            if ( field.isAnnotationPresent( GeneratedValue.class ))
                continue;
                // Значение может генерироваться, ничего не делаем
            // проверяем значние поля на NULL
            if (field.get( object) ==null){
                System.out.println(clazz.getSimpleName() + "."+ field.getName() + ": @ID is NULL");
                break; // дальнейшая проверка не имеет смысла
            }
        }
    }
}
