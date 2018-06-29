package ru.sbt.examples.dinamic.cash.proxy;

import ru.sbt.examples.dinamic.cash.service.ServiceImpl;
import ru.sbt.examples.dinamic.cash.service.ServiceProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class CacheProxy {

    public CacheProxy() {
    }

    public Object cache(Object object){
        InvocationHandler invocationHandler=null;
        if(ServiceImpl.class.equals(object.getClass())){
            invocationHandler=new ServiceProxy(object);
        }

        if(invocationHandler==null){
            return object;
        }

        return Proxy.newProxyInstance(object.getClass().getClassLoader(),
                object.getClass().getInterfaces(),
                invocationHandler);
    }
}
