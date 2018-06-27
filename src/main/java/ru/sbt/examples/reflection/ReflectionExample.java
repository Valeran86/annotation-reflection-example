package ru.sbt.examples.reflection;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

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
        System.out.println("Результат 1.Вывести на консоль все методы класса Double, включая все родительские методы  (включая приватные)");
        Class<?> classes=Double.class;
        while(classes!=null){
            System.out.println("Методы класса "+classes.getName());

            for (Method method:classes.getDeclaredMethods()){
                System.out.println(classes.getName()+":"+method.getName() );
            }
            classes=classes.getSuperclass();
        }

        System.out.println("Результат 2. Вывести все геттеры класса Class");

        Method[] methodsClass=Class.class.getDeclaredMethods();
        for(Method method:methodsClass){
            if(isGetter(method)){
                System.out.println( method.getName() );
            }
        }

        System.out.println("Результат 3. Проверить, что все String константы имеют значение = их имени");
        Field[] fields = ReflectionExample.class.getDeclaredFields();
        for(Field field : fields) {
            if(!field.getType().equals(String.class)){
                continue;
            }

            if(Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
                try{
                    String fieldValue=String.valueOf(field.get(null));
                    if(fieldValue!=null && field.getName().toLowerCase().equals(fieldValue.toLowerCase())){
                        System.out.println("OK:Константа "+field.getName() + " имеет значение равное её имени");
                    }else{
                        System.out.println("WARNING:Константа "+field.getName() + " имеет значение не равное её имени");
                    }

                }catch (IllegalAccessException error){
                    error.printStackTrace();
                }
            }
        }

        Number n = Double.valueOf( 1d );
        System.out.println( n );
        System.out.println( ReflectionToStringBuilder.toString( n ) );
    }

    private static boolean isGetter(Method method){
        return ((method.getName().startsWith("get") || method.getName().startsWith("is"))
                && method.getParameterCount()  == 0 && !void.class.equals(method.getReturnType()));
    }
}
