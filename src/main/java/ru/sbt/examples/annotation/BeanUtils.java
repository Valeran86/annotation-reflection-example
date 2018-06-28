package ru.sbt.examples.annotation;

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
            List<Method> fromGetters = new ArrayList<Method>(  );
            Method[] fromMethods = fromClass.getMethods();

            for (Method method : fromMethods){
                if( isGetter( method ) ) fromGetters.add( method );
            }


            Class toClass = to.getClass();
            Method[] toMethods = toClass.getMethods();

            Method fromGetter = null;

            for ( Method method : toMethods ){
                fromGetter = setterEqualGetter( method, fromGetters );

                if (( isSetter( method ) ) && ( fromGetter!=null )) {

                    Method getMethod = fromClass.getMethod( fromGetter.getName(), null );
                    Object returnValue = getMethod.invoke( from );

                    Method setMethod = toClass.getMethod( method.getName(), method.getParameterTypes()[0] );
                    setMethod.invoke( to, returnValue );

                }
            }
        }

        private static Method setterEqualGetter (Method setter, List<Method> getters){

            Class[] setParams = setter.getParameterTypes();
            if (setParams.length == 0) return null;

            for(Method testGetter : getters){

                Class getParam = testGetter.getReturnType();

                if ((getParam.equals( setParams[0] )) && (namesEquals( testGetter.getName(), setter.getName() ))){
                    return testGetter;
                }
            }

            return null;
        }

        private static boolean namesEquals (String getName, String setName){

            if (getName.substring( 3 ).equals( setName.substring( 3 ) )) return true;
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
