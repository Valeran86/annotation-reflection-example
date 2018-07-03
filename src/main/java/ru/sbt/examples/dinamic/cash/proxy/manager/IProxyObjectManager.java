package ru.sbt.examples.dinamic.cash.proxy.manager;

import ru.sbt.examples.dinamic.cash.proxy.ProxyObject;
import ru.sbt.examples.dinamic.cash.proxy.annotation.CacheDynamicProxy;

import java.lang.reflect.Method;
import java.util.HashMap;


public interface IProxyObjectManager {

    ProxyObject proxyObjectContains(Method method, HashMap<Class, Object> hashMapAttr,
                                    HashMap<String,Object> hashMapAttrAnnotation);

    void addReturnValueInProxyObject(ProxyObject proxyObject,Object returnValue);

    void initialConfiguration(HashMap<String,Object> hashMapAttrAnnotation);
}
