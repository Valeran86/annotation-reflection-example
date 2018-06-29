package ru.sbt.examples.cacheproxy;

import java.lang.reflect.Proxy;

public class UseCacheProxy {

    public static void main( String[] args ) {

        ServiceImpl service = new ServiceImpl();
        Service serviceProxy = (Service) Proxy.newProxyInstance(
                                            ServiceImpl.class.getClassLoader(),
                                            ServiceImpl.class.getInterfaces(),
                                            new CacheHandler( service ));

        serviceProxy.factorial( 10 );

        System.out.println(serviceProxy.toString());
    }

}
