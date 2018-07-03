package ru.sbt.examples.dinamic.cash.proxy;

import ru.sbt.examples.dinamic.cash.proxy.annotation.CacheDynamicProxy;
import ru.sbt.examples.dinamic.cash.proxy.constants.Constants;
import ru.sbt.examples.dinamic.cash.proxy.manager.FileProxyObjectManager;
import ru.sbt.examples.dinamic.cash.proxy.manager.IProxyObjectManager;
import ru.sbt.examples.dinamic.cash.proxy.manager.MemoryProxyObjectManager;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class ProxyObjectManager {
    private static IProxyObjectManager proxyObjectManager;

    public static void createProxyObjectManager(String typeCache){
        switch (typeCache){
            case Constants.IN_MEMORY:
                proxyObjectManager=new MemoryProxyObjectManager();
                break;
            case Constants.IN_FILE:
                proxyObjectManager=new FileProxyObjectManager();
                break;
            default:
                throw new IllegalArgumentException("ERROR: Отсутствует тип кэширования. Возможно в аннотации " +
                        "CacheDynamicProxy установлен тип кэширования, обработка которого отсутствует.");
        }
    }

    public static HashMap<String,Object> createHashMapAttrAnnotation(CacheDynamicProxy annotationCacheDynamicProxy, Method method){
        HashMap<String,Object> hashMapAttrAnnotation=new HashMap<>();
        String typeCache=annotationCacheDynamicProxy.saveCacheType();
        int countList=annotationCacheDynamicProxy.countList();
        if(countList>=0 && method.getReturnType().equals(List.class)){
            hashMapAttrAnnotation.put(Constants.IS_CACHE_NEW_LIST,true);
            hashMapAttrAnnotation.put(Constants.COUNT_LIST,countList);
        }
        if((Constants.IN_FILE).equals(typeCache)){
            if(annotationCacheDynamicProxy.fileName().length()>0){
                hashMapAttrAnnotation.put(Constants.FILE_NAME,annotationCacheDynamicProxy.fileName());
            }else{
                hashMapAttrAnnotation.put(Constants.FILE_NAME,method.getName());
            }

        }

        proxyObjectManager.initialConfiguration(hashMapAttrAnnotation);

        return hashMapAttrAnnotation;
    }


    public static ProxyObject proxyObjectContains(Method method, HashMap<Class, Object> hashMapAttr,
                  HashMap<String,Object> hashMapAttrAnnotation){
        return  proxyObjectManager.proxyObjectContains(method,hashMapAttr,hashMapAttrAnnotation);
    }

    public static void addReturnValueInProxyObject(ProxyObject proxyObject,Object returnValue){
        proxyObjectManager.addReturnValueInProxyObject(proxyObject,returnValue);
    }

}
