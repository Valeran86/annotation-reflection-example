package ru.sbt.examples.dinamic.cash.service;

import ru.sbt.examples.dinamic.cash.proxy.ProxyObject;
import ru.sbt.examples.dinamic.cash.proxy.ProxyObjectManager;
import ru.sbt.examples.dinamic.cash.proxy.annotation.CacheDynamicProxy;
import ru.sbt.examples.dinamic.cash.proxy.constants.Constants;

import java.lang.reflect.Method;
import java.util.*;

public class ServiceProxy implements java.lang.reflect.InvocationHandler {
    private Object obj;

    public ServiceProxy(Object f1){
        obj = f1;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
         if(!method.isAnnotationPresent(CacheDynamicProxy.class)){
             return method.invoke(obj, args) ;
         }

         try {
             CacheDynamicProxy annotationCacheDynamicProxy = method.getAnnotation(CacheDynamicProxy.class);
             ProxyObjectManager.createProxyObjectManager(annotationCacheDynamicProxy.saveCacheType());

             ProxyObject proxyObject = getCacheProxyObject(annotationCacheDynamicProxy, method, args);

             return returnValueFromCache(proxyObject, method, args);
         }catch (Exception ex){
             ex.printStackTrace(System.out);
         }
        return method.invoke(obj, args);
    }

    private ProxyObject getCacheProxyObject(CacheDynamicProxy annotationCacheDynamicProxy, Method method, Object[] args){
        HashMap<Class,Object> hashMapParamValue=new HashMap<>();
        List identityBy=Arrays.asList(annotationCacheDynamicProxy.identityBy());
        if(args!=null){
            for(Object arg:args){
                if(identityBy.size()==0 ||identityBy.contains(arg.getClass())){
                    hashMapParamValue.put(arg.getClass(),arg);
                }
            }
        }

        HashMap<String,Object> hashMapAttrAnnotation=ProxyObjectManager.createHashMapAttrAnnotation(annotationCacheDynamicProxy,method);
        return  returnProxyObjectOnTypeCache(method,hashMapParamValue,hashMapAttrAnnotation);
    }

    private ProxyObject returnProxyObjectOnTypeCache(Method method, HashMap<Class, Object> hashMapAttr,
                 HashMap<String,Object> hashMapAttrAnnotation){
        return ProxyObjectManager.proxyObjectContains(method,hashMapAttr,hashMapAttrAnnotation);
    }

    private Object returnValueFromCache(ProxyObject proxyObject, Method method, Object[] args)
            throws Throwable{

        Object returnValue=proxyObject.getReturnValue();
        if (returnValue!=null){
            System.out.println("INFO: Значение взято из кэша");
        } else {
            System.out.println("INFO: Значение расчитано");
            Object noCacheResult=method.invoke(obj, args);
            returnValue=noCacheResult;
            HashMap<String,Object> hashMapAttrAnnotation=proxyObject.getHashMapAttrAnnotation();
            Object isCacheNewList=hashMapAttrAnnotation.get(Constants.IS_CACHE_NEW_LIST);

            if(isCacheNewList!=null && (Boolean)isCacheNewList){
                int countList=(Integer) hashMapAttrAnnotation.get(Constants.COUNT_LIST);
                noCacheResult=(((List)noCacheResult).subList(0, countList));
            }

            ProxyObjectManager.addReturnValueInProxyObject(proxyObject,noCacheResult);

        }
        return returnValue;
    }



}
