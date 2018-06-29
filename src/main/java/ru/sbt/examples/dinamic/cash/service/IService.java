package ru.sbt.examples.dinamic.cash.service;

import org.hibernate.annotations.Cache;
import ru.sbt.examples.dinamic.cash.proxy.annotation.CacheDynamicProxy;
import ru.sbt.examples.dinamic.cash.proxy.constants.Constants;

import java.util.List;

public interface IService {
    @CacheDynamicProxy()
    public Double doHardWork(String param1,Integer param2);
    @CacheDynamicProxy(countList=100, saveCacheType = Constants.IN_MEMORY)
    public List<String> doListMoreLines();

}
