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
        //Задача закрыта. Убрал чтобы не отвлекало.
    }

}
