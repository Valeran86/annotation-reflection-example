package ru.sbt.examples.annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

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

          /*
            Сканируем объект "from" на все геттеры.
            Если объект "to" содержит соответствующий сеттер,
            то следует вызвать его для установления значения,
            которое берем из свойства "from".
            Тип сеттера должен быть совместим с типом значения которое возвращает геттер
            (если это не так, то вызов не выполняем).
            Совместимость означает, что тип параметра в сеттере должен совпадать или быть
            суперклассом возвращаемого типа геттера.
            Метод работает только с public методами.
            @param to -объект, свойства которого будут установлены. (т.е. тут вызывает сеттеры со значениями геттеров из объекта from)
            @param from - объект свойства которого будут использоваться для получения значений. (т.е. тут вызывает геттеры)
           */


        public static void assign(Object to, Object from) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

            Class fromClass = from.getClass();
            Class toClass = to.getClass();

            Map<String, Method> fromGetters = new HashMap<String, Method>(  );
            Map<String, Method> toSetters = new HashMap<String, Method>(  );

            Method[] fromMethods = fromClass.getMethods();
            Method[] toMethods = toClass.getMethods();

            for (Method method : fromMethods){
                if( isGetter( method ) ) fromGetters.put( method.getName().substring( 3 ), method );
            }

            for (Method method : toMethods){
                String key = method.getName().substring( 3 );
                if ((fromGetters.containsKey( key )) && ( isSetter( method ) ) && (isParamEquals(method, fromGetters.get( key )))){

                    //На всякий случай добавлю в ХешМэп, хотя можно без него обойтись
                    toSetters.put( method.getName().substring( 3 ), method );

                    Method getMethod = fromClass.getMethod( fromGetters.get( key ).getName(), null );
                    Object returnValue = getMethod.invoke( from );

                    Method setMethod = toClass.getMethod( method.getName(), method.getParameterTypes()[0] );
                    setMethod.invoke( to, returnValue );

                }
            }

        }

        private static boolean isParamEquals(Method setter, Method getter){
            Class[] setParams = setter.getParameterTypes();
            if (setParams.length == 0) return false;

            Class getParam = getter.getReturnType();
            if ((getParam.equals( setParams[0] ))) return true;

            return false;
        }

        private static boolean isGetter(Method method){
            if(!method.getName().startsWith("get"))      return false;
            if(method.getParameterTypes().length != 0)   return false;
            if(void.class.equals(method.getReturnType())) return false;
            return true;
        }

        private static boolean isSetter(Method method){
            if(!method.getName().startsWith("set")) return false;
            if(method.getParameterTypes().length != 1) return false;

            return true;
        }
}
