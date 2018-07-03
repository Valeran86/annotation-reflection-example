package ru.sbt.examples.reflection;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

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

        System.out.println( n );
        System.out.println( ReflectionToStringBuilder.toString( n ) );

        printAllMethodsOfDoubleClass();
        printAllGettersOfDoubleClass();
        checkConstNames();
    }
    static void checkConstNames () {
        for (Field field :ReflectionExample.class.getFields()) {
            System.out.print(field.getName() + " ");
            field.setAccessible(true);
            try {
                if (field.getName().equals(field.get(null).toString())) {
                    System.out.println("true");
                } else {
                    System.out.println("false");
                }
            }
            catch (IllegalAccessException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    static void printAllMethodsOfDoubleClass() {
        Double d = new Double(1d);
        Class temp = d.getClass();
        do {
            printAll(temp.getDeclaredMethods());
            temp = temp.getSuperclass();
        }
        while (temp != Object.class);
    }
    static void printAllGettersOfDoubleClass() {
        Class temp = Class.class;
        printAll(Arrays.stream(temp.getDeclaredMethods()).filter(i -> isGetter(i)).toArray());
    }
    static void printAll(Object[] names) {
        for (Object name: names) {
            System.out.println(name.toString());
        }
        System.out.println(names.length);
    }
    public static boolean isGetter(Method method){
        if(!method.getName().startsWith("get"))      return false;
        if(method.getParameterTypes().length != 0)   return false;
        if(void.class.equals(method.getReturnType())) return false;
        return true;
    }
}
