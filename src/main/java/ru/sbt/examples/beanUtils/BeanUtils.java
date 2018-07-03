package ru.sbt.examples.beanUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static ru.sbt.examples.reflection.ReflectionExample.isGetter;

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
    public static void assign(Object to, Object from) throws InvocationTargetException, IllegalAccessException {
        Class f = from.getClass();
        Class t = to.getClass();
        Map<String, Method> getters = new HashMap<>();
        Object[] g = Arrays.stream(f.getDeclaredMethods()).filter(i -> isGetter(i)).toArray();
        for (Object o: g) {
            getters.put(((Method)o).getName().substring(3), ((Method)o));
        }
        for (Method m: t.getMethods()) {
            if (getters.containsKey(m.getName().substring(3)) && isSetter(m)) {
                Method possibleGetter = getters.get(m.getName().substring(3));
                if (isCompatibleTypes(possibleGetter.getReturnType(), m.getReturnType())) {
                    Object value = possibleGetter.invoke(from);
                    m.invoke(to, value);
                }
            }
        }
    }
    private static boolean isSetter(Method method){
        if(!method.getName().startsWith("set")) return false;
        if(method.getParameterTypes().length != 1) return false;
        return true;
    }
    private static boolean isCompatibleTypes(Class<?> getter, Class<?> setter) {
        Class<?> copy = setter;
        while (!copy.equals(getter)) {
            copy = copy.getSuperclass();
        }
        if (Object.class.equals(copy))
            return false;
        return true;
    }
}

