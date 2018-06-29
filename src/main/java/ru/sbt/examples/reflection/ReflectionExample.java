package ru.sbt.examples.reflection;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.common.reflection.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

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
        System.out.println("111111111111111111111111111111");
        Task1 (n.getClass());
        System.out.println("222222222222222222222222222222");
        Task2 (ArrayList.class );
        System.out.println("33333333333333333333333333333");
        Task3 (new ReflectionExample());
    }

    public static   void Task1 (Class   clazz ){
        if (clazz == null)
            return;
        for (Method method:        clazz.getDeclaredMethods()){
            System.out.println(clazz.getName() + " ." + method.getName());
        }
        Task1 (clazz.getSuperclass());
    }

    public static  void Task2 (Class <?> clazz){
        for (  Method method:  clazz.getMethods() ) {
            if (method.getName().startsWith( "get" ))
                System.out.println(method.getName());
        }
    }

    public static   void Task3 (Object someclass)  {
        if (someclass ==  null)
            return;
        // необходимо убедиться, что передали экзмепляр класса
        if (someclass instanceof  Class)
            return;
        Class <?> clazz = someclass.getClass();
        // проверям все public static final  поля типа String у класса clazz
        // вопрос : 1. как получить доступ к final static полям через рефлексию без передачи ссылки на экземпляр класса ?

        for ( Field field: clazz.getFields()) {
            if (field.getType() != java.lang.String.class  || !Modifier.isFinal( field.getModifiers()  )  || !Modifier.isStatic( field.getModifiers() ))
                continue;
            try {
                String value = (String) field.get( someclass );
                if (!(field.getName().equals( value ))){
                    System.out.printf("Field [%s] != %s ",field.getName(),value);
                }
            }
            catch ( IllegalAccessException ex ){

            }


        }

    }
}
