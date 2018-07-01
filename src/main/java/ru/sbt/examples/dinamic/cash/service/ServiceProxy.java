package ru.sbt.examples.dinamic.cash.service;

import ru.sbt.examples.dinamic.cash.proxy.ProxyObject;
import ru.sbt.examples.dinamic.cash.proxy.ProxyObjectManager;
import ru.sbt.examples.dinamic.cash.proxy.annotation.CacheDynamicProxy;
import ru.sbt.examples.dinamic.cash.proxy.constants.Constants;

import java.lang.reflect.Method;
import java.util.*;

public class ServiceProxy implements java.lang.reflect.InvocationHandler {
    private Object obj;
    private boolean isCasheNewList=false;
    private int countList=-1;

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

             ProxyObject proxyObject = getCacheProxyObject(annotationCacheDynamicProxy, method, args);

             return returnValueFromCache(proxyObject, method, args, annotationCacheDynamicProxy.saveCacheType());
         }catch (Exception ex){
             ex.printStackTrace(System.out);
         }
        return method.invoke(obj, args);
    }

    private ProxyObject getCacheProxyObject(CacheDynamicProxy annotationCacheDynamicProxy, Method method, Object[] args){
        HashMap<Class,Object> hashMapParamValue=new HashMap<>();
        HashMap<String,Object> hashMapAttrAnnotation=new HashMap<>();
        String typeCache=annotationCacheDynamicProxy.saveCacheType();

        List identityBy=Arrays.asList(annotationCacheDynamicProxy.identityBy());
        if(args!=null){
            for(Object arg:args){
                if(identityBy.size()==0 ||identityBy.contains(arg.getClass())){
                    hashMapParamValue.put(arg.getClass(),arg);
                }
            }
        }
        int countList=annotationCacheDynamicProxy.countList();
        if(countList>=0 && method.getReturnType().equals(List.class)){
            this.isCasheNewList=true;
            hashMapAttrAnnotation.put("countList",countList);
            this.countList=countList;
        }
        if(typeCache.equals(Constants.IN_FILE)){
            if(annotationCacheDynamicProxy.fileName().length()>0){
                ProxyObjectManager.setFileName(annotationCacheDynamicProxy.fileName());
            }else{
                ProxyObjectManager.setFileName(method.getName());
            }

        }

        return  returnProxyObjectOnTypeCache(method,hashMapParamValue,hashMapAttrAnnotation,typeCache);

    }

    private ProxyObject returnProxyObjectOnTypeCache(Method method, HashMap<Class, Object> hashMapAttr,
                 HashMap<String,Object> hashMapAttrAnnotation, String typeCache){
        switch (typeCache){
            case Constants.IN_MEMORY:
                return  ProxyObjectManager.proxyObjectContainsMemory(method,hashMapAttr,hashMapAttrAnnotation);
            case Constants.IN_FILE:
                return ProxyObjectManager.proxyObjectContainsFile(method,hashMapAttr,hashMapAttrAnnotation);
            default:
                throw new IllegalArgumentException("ERROR: Отсутствует тип кэширования. Возможно в аннотации " +
                        "CacheDynamicProxy установлен тип кэширования, обработка которого отсутствует.");
        }

    }

    private Object returnValueFromCache(ProxyObject proxyObject, Method method, Object[] args, String typeCache)
            throws Throwable{

        Object returnValue=proxyObject.getReturnValue();
        if (returnValue!=null){
            System.out.println("INFO: Значение взято из кэша");
        } else {
            System.out.println("INFO: Значение расчитано");
            Object noCacheResult=method.invoke(obj, args);
            returnValue=noCacheResult;
            if(isCasheNewList){
                noCacheResult=(((List)noCacheResult).subList(0, this.countList));
                isCasheNewList=false;
                this.countList=0;
            }

            ProxyObjectManager.addReturnValueInProxyObject(proxyObject,noCacheResult,typeCache);

        }
        return returnValue;
    }



}
