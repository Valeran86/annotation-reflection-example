package ru.sbt.examples.reflection;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

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

        //1
        Number n = Double.valueOf( 1d );
        HashSet<Method> methodsSet = new HashSet< Method >(  );
        System.out.println( n );
        System.out.println( ReflectionToStringBuilder.toString( n ) );
        Class testClass1 = n.getClass();

        while ( testClass1 != null ){
            Method[] m = testClass1.getDeclaredMethods();
            methodsSet.addAll( Arrays.asList( m ) );
            testClass1 = testClass1.getSuperclass();
        }
        System.out.println( "Все методы класса Double: " );
        for ( Method m : methodsSet ) {
            //  System.out.println( m.toString() );
        }

        //2
        Class testClass2 = Class.class;
        Method[] m = testClass2.getDeclaredMethods();
        for (Method method: m) {
            if (method.getName().matches("^get[A-Z].*")) {
                //              System.out.println( method );
            }

        }

        //3
        ReflectionExample reflExample = new ReflectionExample();
        Field[] fields = ReflectionExample.class.getDeclaredFields();
        boolean eachNameEqualsValue = true;
        for ( Field field : fields) {
            try {
                String name = field.getName();
                String value = ((String ) field.get(reflExample));
                System.out.println( value );
                if (!name.equals( value ))
                    eachNameEqualsValue = false;

            } catch ( IllegalAccessException e ) {
                e.printStackTrace( );
                eachNameEqualsValue = false;
            }

        }
        System.out.println( eachNameEqualsValue ? "Все константы равны свои значениям" : "Не все константы равны свои значениям"  );
    }
}


