package ru.sbt.examples.dinamic.cash.service;

import ru.sbt.examples.dinamic.cash.proxy.annotation.CacheDynamicProxy;
import ru.sbt.examples.dinamic.cash.proxy.constants.Constants;

import java.lang.reflect.Method;
import java.util.*;
import java.io.*;

public class ServiceProxy implements java.lang.reflect.InvocationHandler {
    private Object obj;
    private HashMap<List<Object>,Object> cacheHashyMap=new HashMap<>();
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
        List<Object> paramsCache=getParamsCache(annotationCacheDynamicProxy,method,args);

        if(cacheHashyMap.containsKey(paramsCache)){
            System.out.println("INFO: Значение взято из кэша");
            return cacheHashyMap.get(paramsCache);
        }else{
            System.out.println("INFO: Значение расчитано");
            Object noCacheResult=method.invoke(obj, args);
            Object inCacheResult=noCacheResult;
            if(isCasheNewList){
                inCacheResult=(((List)noCacheResult).subList(0, this.countList));
                isCasheNewList=false;
                this.countList=0;
            }
            cacheHashyMap.put(paramsCache,(inCacheResult));

            return noCacheResult;
        }
    }

    private List<Object> getParamsCache(CacheDynamicProxy annotationCacheDynamicProxy, Method method, Object[] args){
        List<Object> paramsCache=new ArrayList<>();
        paramsCache.add(method);
        List identityBy=Arrays.asList(annotationCacheDynamicProxy.identityBy());
        HashMap<Class,Object> listParamValue=new HashMap<>();
        if(args!=null){
            for(Object arg:args){
                if(identityBy.size()==0 ||identityBy.contains(arg.getClass())){
                    listParamValue.put(arg.getClass(),arg);
                }
            }
            paramsCache.add(listParamValue);
        }

        int countList=annotationCacheDynamicProxy.countList();
        if(countList>=0 && method.getReturnType().equals(List.class)){
            this.isCasheNewList=true;
            paramsCache.add("List size:"+countList);
            this.countList=countList;
        }


        return paramsCache;
    }

    private Object casheFromFile(CacheDynamicProxy annotationCacheDynamicProxy, Method method, Object[] args)
            throws Throwable{
        List<Object> paramsCache=getParamsCache(annotationCacheDynamicProxy,method,args);
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
