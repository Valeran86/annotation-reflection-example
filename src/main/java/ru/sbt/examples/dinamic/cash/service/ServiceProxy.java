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

         CacheDynamicProxy annotationCacheDynamicProxy=method.getAnnotation(CacheDynamicProxy.class);

         switch (annotationCacheDynamicProxy.saveCacheType()){
             case Constants.IN_MEMORY:
                 return casheFromMemory(annotationCacheDynamicProxy,method,args);
             case Constants.IN_FILE:
                 return casheFromFile(annotationCacheDynamicProxy,method,args);
             default:
                 return method.invoke(obj, args) ;
         }
    }

    private Object casheFromMemory(CacheDynamicProxy annotationCacheDynamicProxy, Method method, Object[] args)
            throws Throwable{
        ProxyObject proxyObject=getCasheProxyObject(annotationCacheDynamicProxy,method,args);
        Object returnValue=proxyObject.getReturnValue();
        if (returnValue!=null){
            System.out.println("INFO: Значение взято из кэша");
        }else {
            System.out.println("INFO: Значение расчитано");
            Object noCacheResult=method.invoke(obj, args);
            returnValue=noCacheResult;
            if(isCasheNewList){
                noCacheResult=(((List)noCacheResult).subList(0, this.countList));
                isCasheNewList=false;
                this.countList=0;
            }

            ProxyObjectManager.addReturnValueInProxyObject(proxyObject,noCacheResult);

        }
        return returnValue;
    }


    private ProxyObject getCasheProxyObject(CacheDynamicProxy annotationCacheDynamicProxy, Method method, Object[] args){
        HashMap<Class,Object> listParamValue=new HashMap<>();
        HashMap<String,Object> hashMapAttrAnnotation=new HashMap<>();

        List identityBy=Arrays.asList(annotationCacheDynamicProxy.identityBy());
        if(args!=null){
            for(Object arg:args){
                if(identityBy.size()==0 ||identityBy.contains(arg.getClass())){
                    listParamValue.put(arg.getClass(),arg);
                }
            }
        }
        int countList=annotationCacheDynamicProxy.countList();
        if(countList>=0 && method.getReturnType().equals(List.class)){
            this.isCasheNewList=true;
            hashMapAttrAnnotation.put("countList",countList);
            this.countList=countList;
        }

        return  ProxyObjectManager.proxyObjectContains(method,listParamValue,hashMapAttrAnnotation);

    }

    private Object casheFromFile(CacheDynamicProxy annotationCacheDynamicProxy, Method method, Object[] args)
            throws Throwable{

//TODO предстоит сделать работу с файлами
        if(/*Есть в файле*/1!=1){
            System.out.println("INFO: Значение взято из файла");
            return new Object()/*Из файла*/;
        }else{
            System.out.println("INFO: Значение расчитано");
            Object noCacheResult=method.invoke(obj, args);


            return noCacheResult;
        }
    }



}
