package ru.sbt.examples.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
    public static void assign(Object to, Object from) {
        Class<?> classTo=to.getClass();
        Class<?> classFrom=from.getClass();
        List<Method> methodsToSet=new ArrayList<>();
        List<Method> methodsFromGet=new ArrayList<>();
        for(Method methodFrom:classFrom.getMethods()){
            if(isGetter((methodFrom))){
                methodsFromGet.add(methodFrom);
            }
        }
        for(Method methodTo:classTo.getMethods()){
            if(isSetter((methodTo))){
                methodsToSet.add(methodTo);
            }
        }
        for(Method methodFromGet:methodsFromGet){
            assign(methodFromGet, methodsToSet, to, from);
        }



    }

    private static boolean isGetter(Method method){
        if(!method.getName().startsWith("get") && !method.getName().startsWith("is"))      return false;
        if(method.getParameterTypes().length != 0)   return false;
        if(void.class.equals(method.getReturnType())) return false;
        return true;
    }

    private static boolean isSetter(Method method){
        if(!method.getName().startsWith("set")) return false;
        if(method.getParameterTypes().length != 1) return false;
        return true;
    }

    private static void assign(Method methodFromGet, List<Method> methodsToSet,Object to,Object from){
        String nameMethodFromGet=methodFromGet.getName().startsWith("get")?methodFromGet.getName().substring(3):methodFromGet.getName().substring(2);
        Class<?> typeReturnMethodFromGet=methodFromGet.getReturnType();
        for(Method methodToSet:methodsToSet){
            if(methodToSet.getName().substring(3).equals(nameMethodFromGet) && isEqulsClass(typeReturnMethodFromGet,methodToSet)){
                try {
                    methodToSet.invoke(to,methodFromGet.invoke(from,null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace(System.out);
                } catch (InvocationTargetException e) {
                    e.printStackTrace(System.out);
                }
            }
        }
    }

    private static boolean isEqulsClass(Class<?> typeReturnMethodFromGet, Method methodToSet){
        Class<?> classes=methodToSet.getParameterTypes()[0];
        while(classes!=null){
            if(typeReturnMethodFromGet.equals(classes)){
                return true;
            }
            classes=classes.getSuperclass();
        }
        return  false;
    }

}
