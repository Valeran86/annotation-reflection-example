package ru.sbt.examples.cachedProxy.cachedService;


import java.lang.reflect.Proxy;


public class CachedServiceFactory {
    public static Object newInstance( Object object ) {
        return Proxy.newProxyInstance( object.getClass().getClassLoader(),
                object.getClass().getInterfaces(),
                new CachedServiceHandler( object ) );
    }
}