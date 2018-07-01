package ru.sbt.examples.reflection;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    public static final String BAD_CONSTANT_NAME = "BAD_CONSTANT_NAME";
    //    public static final String bad_constant_name = "bad_constant_name";
    public static final String FAIL_CONSTANT = "FAIL_CONSTANT";
//    public static final String FAIL_CONSTANT = "bad_constant_name";

    public static void main(String... args) throws IllegalAccessException {
        Number n = 1d;

        System.out.println(n);
        System.out.println(ReflectionToStringBuilder.toString(n));

        ReflectionExample re = new ReflectionExample();
        //1.Вывести на консоль все методы класса Double, включая все родительские методы  (включая приватные)
        re.printDeclaredMethods(n.getClass());

        //2.Вывести все геттеры класса Class
        re.printGettersSetters(TestClass.class);

        //3. Проверить, что все String константы имеют значение = их имени
        System.out.println(re.checkConstants(ReflectionExample.class));
    }

    public void printDeclaredMethods(Class c) {
        System.out.println("--- Declared methods of " + c.getName() + " ---------------------------------------");

        for (Method method : c.getDeclaredMethods()) {
            System.out.format( "%s %s(%s)\n"
                    , Modifier.toString(method.getModifiers())
                    , method.getName()
                    , Arrays.toString( method.getParameters() ).replaceAll( "[\\[\\]]","" ) );
        }
    }

    public void printGettersSetters(Class c) {
        System.out.println("--- Getters of " + c.getName() + " ------------------------------------");
        for (Method method : getGetters(c)) {
            System.out.println(method.getName());
        }
        System.out.println("--- Setters of " + c.getName() + " ------------------------------------");
        for (Method method : getSetters(c)) {
            System.out.println(method.getName());
        }
    }

    public List<Method> getGetters(Class c) {
        List<Method> result = new ArrayList<>();
        List<Field> fields = Arrays.stream(c.getDeclaredFields())
                .filter(field -> fieldIsModifiers( field,  Modifier.PRIVATE, ~Modifier.STATIC))
                .collect(Collectors.toList());

        for (Method method : c.getMethods()) {
            if ((method.getName().startsWith("get") || method.getName().startsWith("is"))
                    && method.getParameterCount() == 0) {
                fields.forEach(field -> {
                    if (field.getType().equals(method.getReturnType())
                            && method.getName().toLowerCase()
                            .startsWith(field.getName().toLowerCase()
                                    , method.getReturnType().equals(boolean.class) ? 2 : 3))
                        result.add(method);
                });
            }
        }
        return result;
    }

    public List<Method> getSetters(Class c) {
        List<Method> result = new ArrayList<>();
        List<Field> fields = Arrays.stream(c.getDeclaredFields())
                .filter(field -> fieldIsModifiers( field, Modifier.PRIVATE, ~Modifier.STATIC))
                .collect(Collectors.toList());

        for (Method method : c.getMethods()) {
            if (method.getName().startsWith("set")
                    && method.getParameterCount() == 1
                    && method.getReturnType().equals(void.class)) {
                fields.forEach(field -> {
                    if (method.getParameters()[0].getType().equals(field.getType())
                            && method.getName().toLowerCase().startsWith(field.getName().toLowerCase(), 3))
                        result.add(method);
                });
            }
        }
        return result;
    }

    public boolean checkConstants(Class c) throws IllegalAccessException {
        boolean ret = true;
        List<Field> fields = Arrays.stream(c.getDeclaredFields())
                .filter(field -> fieldIsModifiers( field, Modifier.STATIC, Modifier.FINAL )
                        && field.getType() == String.class )
                .collect(Collectors.toList());

        for (Field field : fields) {
            ret &= field.getName().equals(field.get(this))
                    && field.getName().equals(field.getName().toUpperCase());
        }

        return ret;
    }

    private static boolean fieldIsModifiers(Field field, int... modifiers){
        boolean ret = true;
        for ( int modifier : modifiers ) {
            ret &= (modifier < 0 ? (field.getModifiers() | modifier)
                                 : (field.getModifiers() & modifier)) == modifier;
        }
        return ret;
    }
}

