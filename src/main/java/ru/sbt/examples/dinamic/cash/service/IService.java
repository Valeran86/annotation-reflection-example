package ru.sbt.examples.dinamic.cash.service;

import org.hibernate.annotations.Cache;
import ru.sbt.examples.dinamic.cash.proxy.annotation.CacheDynamicProxy;
import ru.sbt.examples.dinamic.cash.proxy.constants.Constants;

import java.util.List;

public interface IService {
    @CacheDynamicProxy(saveCacheType =Constants.IN_FILE, fileName = "hard2")
    Double doHardWork(String param1,Integer param2);
    @CacheDynamicProxy(countList=100)
    List<String> doListMoreLines();

}
