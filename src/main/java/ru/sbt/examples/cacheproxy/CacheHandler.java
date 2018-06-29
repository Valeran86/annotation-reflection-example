package ru.sbt.examples.cacheproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class CacheHandler implements InvocationHandler {

    private Object obj;

    private CacheManager cacheManager = new CacheManager();

    public CacheHandler(Object service){
        obj = service;
    }


    public Object invoke( Object proxy, Method method, Object[] args ) throws Throwable {

        //Вот тут будем хэшировать
        //Но почемуто у метода не видно моей аннотации, хотя она есть

        if (method.isAnnotationPresent( Cacheble.class )){
            Cacheble cacheble = method.getAnnotation( Cacheble.class );
            System.out.println(cacheble.isCacheble());
        }

        System.out.println("SomeInvocationHandler invoke : " + method.getName());

        return method.invoke( obj, args );

    }

}
