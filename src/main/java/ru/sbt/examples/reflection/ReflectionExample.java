package ru.sbt.examples.reflection;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

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
        //Number n = Double.valueOf( 1d );
        //System.out.println( n );
        //System.out.println( ReflectionToStringBuilder.toString( n ) );

        System.out.println("Все методы класса String, включая все родительские методы  (включая приватные)");

        Class myclass = String.class;
        while ( myclass!=null ){
            System.out.println("\nМетоды класса " + myclass.getName() + ":\n");
            Method[] methods = myclass.getDeclaredMethods();
            for ( int i = 0; i < methods.length; i++ ) {
                System.out.println(methods[i].getName());
            }
            myclass = myclass.getSuperclass();
        }
    }
}
