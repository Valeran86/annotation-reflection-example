package ru.sbt.examples.dinamic.cash.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProxyObjectManager {
    private static List<ProxyObject> listProxyObject=new ArrayList<>();

    public static ProxyObject newProxyObjectList(Method metod, HashMap<Class, Object> hashMapAttr, HashMap<String,Object> hashMapAttrAnnotation, Object returnValue){
        ProxyObject proxyObject= new ProxyObject(metod,hashMapAttr,hashMapAttrAnnotation,returnValue);
        listProxyObject.add(proxyObject);
        return proxyObject;
    }

    public static List<ProxyObject> allProxyObject(){
        List<ProxyObject> newListProxyObject=new ArrayList<>();
        newListProxyObject.addAll(listProxyObject);
        return  newListProxyObject;
    }

    public static ProxyObject proxyObjectContains(Method metod, HashMap<Class, Object> hashMapAttr, HashMap<String,Object> hashMapAttrAnnotation){
        ProxyObject newProxyObject= new ProxyObject(metod,hashMapAttr,hashMapAttrAnnotation,null);
        for(ProxyObject proxyObject:listProxyObject){
            if(newProxyObject.equals(proxyObject)){
                return proxyObject;
            }
        }
        return  newProxyObject;
    }

    public static void addReturnValueInProxyObject(ProxyObject proxyObject,Object returnValue){
        proxyObject.setReturnValue(returnValue);
        listProxyObject.add(proxyObject);
    }

}
