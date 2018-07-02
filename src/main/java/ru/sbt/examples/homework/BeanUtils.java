package ru.sbt.examples.homework;

import com.sun.istack.internal.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanUtils {
    /**
     * Scans object "from" for all getters. If object "to"
     * contains correspondent setter, it will invoke it
     * to set property value for "to" which equals to the property
     * of "from".
     * <p/>
     * The type in setter should be compatible to the value returned
     * by getter (if not, no invocation performed).
     * Compatible means that parameter type in setter should
     * be the same or be superclass of the return type of the getter.
     * <p/>
     * The method takes care only about public methods.
     *
     * @param to   Object which properties will be set.
     * @param from Object which properties will be used to get values.
     */

    public static void assign( @NotNull java.lang.Object to,  @NotNull java.lang.Object from) throws InvocationTargetException, IllegalAccessException {
        //no Check for NPE with use  @NotNull
        for ( Method getter : to.getClass().getDeclaredMethods() ) {
            // проверим что это clean getter: чистый геттер без параметров
            if (!(getter.getName().startsWith( "get" ) && getter.getParameterCount() ==0 ))
                continue;
            Class <?> clazzGetterValue =  getter.getReturnType();
            //  1 проверяем что setter существует для типа clazzValueChanger существует, получаем его имя from
            //  2 получаем значение геттера
            //  3 присваиваем его
            Method setter = getSetterName (clazzGetterValue , from);
            if (setter == null )
                continue;
            java.lang.Object value = getValue (from,getter) ;
            setValue (to,setter,value);
        }
    }

    /**
     *
     * @param clazzGetterValue
     * @param objectTo
     * @return
     */
    private static Method getSetterName (final Class clazzGetterValue ,  java.lang.Object objectTo ){
        for ( Method setter :  objectTo.getClass().getDeclaredMethods()       ) {
            if (!( setter.getName().startsWith( "set" ) && setter.getParameterCount() ==1))
                continue;
            Class <?> setterClass = setter.getParameterTypes()[0];
            Class <?> getterSuperClass = clazzGetterValue;
            while (getterSuperClass != null){
                if (getterSuperClass ==setterClass )
                    return setter;
                getterSuperClass = getterSuperClass.getSuperclass();
            }

        }
        return null;
    }


    /**
     *
     * @param objectTo
     * @param setter
     * @param value
     */
    private static void setValue ( java.lang.Object objectTo ,Method setter,  java.lang.Object  value ){
        try {
            setter.invoke( objectTo,value  );
        }
        catch ( InvocationTargetException ex ){
            System.out.println( ex.toString());

        }
        catch ( IllegalAccessException ex ){
            // т.к. все ctnnths  public и мы идем по getDeclaredMethods ()
            // то  исключений IllegalAccessException  возникнуть не должно
            System.out.println( ex.toString());
        }
    }


    /**
     * @param objectfrom
     * @param method
     * @return
     */
    private static java.lang.Object getValue ( java.lang.Object objectfrom  , Method method  )   {
        java.lang.Object result = null;
        try{
            result = method.invoke( objectfrom );
        }
        catch ( InvocationTargetException ex ){
            // по условию задачи не понятно:  если мы не смогли получить значение при падении геттера,
            // мы возвращаем  InvocationTargetException выше или пытаемся присвоить null
            System.out.println( ex.toString());

        }
        catch ( IllegalAccessException ex ){
            // т.к. все геттеры public и мы идем по getDeclaredMethods ()
            // то  исключений IllegalAccessException  возникнуть не должно
            System.out.println( ex.toString());
        }
        return result;
    }
}
