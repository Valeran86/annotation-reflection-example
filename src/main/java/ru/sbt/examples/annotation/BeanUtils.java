package ru.sbt.examples.annotation;


import org.omg.Dynamic.Parameter;

import java.lang.reflect.*;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


    public static void assign(Object from, Object to) {
        Class < ? > classFrom = from.getClass( );
        Method[] methodsFrom = classFrom.getDeclaredMethods( );
        ArrayList<Method> methodsFromList = new ArrayList< Method >(  );
        methodsFromList.addAll( Arrays.asList( methodsFrom ) );

        Class < ? > classTo = to.getClass( );
        Method[] methodsTo = classTo.getDeclaredMethods( );
        ArrayList<Method> methodsToList = new ArrayList< Method >(  );
        methodsToList.addAll( Arrays.asList( methodsTo ) );

        for (Method method : methodsFromList) {
            if (!Modifier.isPublic( method.getModifiers( )) || (!(method.getName( ).matches( "^get[A-Z].*" )) )) {
                methodsFromList.remove( method );
            }
        }

        for ( Method method : methodsToList ) {
            if (!(Modifier.isPublic( method.getModifiers( ))&&method.getName( ).matches( "^set[A-Z].*" ))) {
                methodsToList.remove( method );
            }
        }

        if ( methodsFromList.size() > 0 && methodsToList.size() > 0 ) {

            for ( Method methodFrom : methodsFromList ) {

                for ( Method methodTo : methodsToList ) {
                    if (methodFrom.getName( ).replace( "get", "set" ).equals( methodTo.getName() )) {
                        // mapAssign.put( methodFrom, methodTo);
                        Class <?> parGet = methodFrom.getReturnType();
                        Class <?> parSet = methodTo.getParameterTypes()[0];
                        if ( parGet.isAssignableFrom( parSet )) {
                            System.out.println( "Подходят " + parGet + parSet );
                            try {
                                Object ret =  methodFrom.invoke( from);
                                methodTo.invoke( to, ret );
                            } catch ( IllegalAccessException e ) {
                                e.printStackTrace( );
                            } catch ( InvocationTargetException e ) {
                                e.printStackTrace( );
                            }
                        }
                    }
                }
            }
        }
        else {
            System.out.println( "В классах From или To нет нужных public методов" );
        }
    }
}