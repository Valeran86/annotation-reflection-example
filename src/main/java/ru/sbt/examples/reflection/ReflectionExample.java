package ru.sbt.examples.reflection;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * I. Рефлексия
 * 1.Вывести на консоль все методы класса Double, включая все родительские методы  (включая приватные)
 * <p>
 * 2. Вывести все геттеры класса Class
 * <p>
 * 3. Проверить, что все String константы имеют значение = их имени
 * public static final String MONDAY = "MONDAY";
 */
public class ReflectionExample {
    public static final String MONDAY = "MONDAY";
    public static final String SECOND_DAY = "SECOND_DAY";
    public static final String bad_constant_name = "bad_constant_name";
    public static final String FAIL_CONSTANT = "bad_constant_name";

    public static void main( String... args ) {
        Number n = Double.valueOf( 1d );
        // 1.Вывести на консоль все методы класса Double, включая все родительские методы  (включая приватные)
        Class clazz = n.getClass();
        while ( clazz != null ) {
            System.out.println( "===============" + clazz.getName() );
            for ( Method method : clazz.getDeclaredMethods() ) {
                System.out.println( method.getName() );
            }
            clazz = clazz.getSuperclass();
        }

        // 2. Вывести все геттеры класса Class
        Class clz = Class.class;
        System.out.println( "===============" + clz.getName() );
        for ( Method method : clz.getDeclaredMethods() ) {
            if ( method.getName().substring( 0, 3 ).equals( "get" ) )
                System.out.println( method.getName() );
        }
        // Проверить, что все String константы имеют значение = их имени
        Class reflExmpl = ReflectionExample.class;
        System.out.println( "===============" + reflExmpl.getName() );
        ReflectionExample reflectionExample = new ReflectionExample();
        Object value = null;
        for ( Field field : reflExmpl.getDeclaredFields() ) {
            try {
                value = field.get(reflectionExample);

            } catch ( IllegalAccessException e ) {
                e.printStackTrace();
            }
            if (!value.equals( field.getName() )){
                System.out.println(field.getName() + " != " + value);
            }
        }

        //System.out.println( n );
        //System.out.println( ReflectionToStringBuilder.toString( n ) );
    }
}
