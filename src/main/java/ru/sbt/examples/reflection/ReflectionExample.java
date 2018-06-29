package ru.sbt.examples.reflection;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.*;

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
//        Number n = Double.valueOf( 1d );
//
//        System.out.println( n );
//        System.out.println( ReflectionToStringBuilder.toString( n ) );
        allMethodsIncludePrivate(Double.class);
        allGettersOfClass(Class.class);
        checkAllConstants(ReflectionExample.class);
    }
    private static void allMethodsIncludePrivate(Class clazz){
        do {
            Method allM[] = clazz.getDeclaredMethods();
            for (int i = 0; i < allM.length; i++) {
                System.out.println(allM[i]);
            }
            clazz = clazz.getSuperclass();
        }while(clazz != null);
    }
    private class My{
        private Double p1;

        public Double getP1() {
            return p1;
        }

        public void setP1(Double p1) {
            this.p1 = p1;
        }
        private void met(){
        }
    }
    private static void allGettersOfClass(Class clazz){
        Field allF[] = clazz.getDeclaredFields();
        Method allM[] = clazz.getDeclaredMethods();
        String str;
        Pattern p1;
        Matcher m1;

        for (int i = 0; i < allF.length; i++) {
            p1 = Pattern.compile("(\\S+\\s\\S+)" + allF[i].getName());
            m1 = p1.matcher(allF[i].toString());
            if(m1.find()) {
                str = ".*\\s" + m1.group(1) + "get" + allF[i].getName().substring(0,1).toUpperCase()+ allF[i].getName().substring(1) + "\\(\\)";
                str= str.replace("$", "\\$");
                str= str.replace("[", "\\[");
                str= str.replace("]", "\\]");
                for (int j = 0; j < allM.length; j++) {
                    if (allM[j].toString().matches(str)) {
                        System.out.println(allM[j]);
                        break;
                    }
                }
            }
        }

    }
    private static <T> void checkAllConstants(Class clazz){
        Field allF[] = clazz.getDeclaredFields();
        boolean acc;
        for (int i=0;i<allF.length;i++){
            if(allF[i].getType() != String.class) continue;
            if(!allF[i].toString().matches(".*\\sfinal\\s.+")) continue;
            acc = allF[i].isAccessible();
            allF[i].setAccessible(true);
            // дальше непонятно как получить значение этой константы, чтобы сравнить с его именем
//            String value=allF[i].get(???);
//            if(value != allF[i].getName()) System.out.println("Константа " + allF[i].getName()
//                    + " имеет значение " + value + " не совпадающее с именем!");
            allF[i].setAccessible(acc);
        }

    }
}
