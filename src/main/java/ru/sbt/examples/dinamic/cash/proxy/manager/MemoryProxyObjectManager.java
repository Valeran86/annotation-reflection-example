package ru.sbt.examples.dinamic.cash.proxy.manager;

import ru.sbt.examples.dinamic.cash.proxy.ProxyObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemoryProxyObjectManager implements IProxyObjectManager {
    private static List<ProxyObject> listProxyObject=new ArrayList<>();

    public static ProxyObject newProxyObjectList(Method metod, HashMap<Class, Object> hashMapAttr, HashMap<String,Object> hashMapAttrAnnotation, Object returnValue){
        ProxyObject proxyObject= new ProxyObject(metod,hashMapAttr,hashMapAttrAnnotation,returnValue);
        listProxyObject.add(proxyObject);
        return proxyObject;
    }

    public static List<ProxyObject> allProxyObject(){
        return new ArrayList<>(listProxyObject);
    }

    @Override
    public void initialConfiguration(HashMap<String,Object> hashMapAttrAnnotation){
    }

    @Override
    public ProxyObject proxyObjectContains(Method method, HashMap<Class, Object> hashMapAttr,
                                                        HashMap<String,Object> hashMapAttrAnnotation){
        ProxyObject newProxyObject= new ProxyObject(method,hashMapAttr,hashMapAttrAnnotation,null);
        for(ProxyObject proxyObject:listProxyObject){
            if(newProxyObject.equals(proxyObject)){
                return proxyObject;
            }
        }
        return  newProxyObject;
    }

    @Override
    public void addReturnValueInProxyObject(ProxyObject proxyObject,Object returnValue){
        proxyObject.setReturnValue(returnValue);
        listProxyObject.add(proxyObject);
    }



}
