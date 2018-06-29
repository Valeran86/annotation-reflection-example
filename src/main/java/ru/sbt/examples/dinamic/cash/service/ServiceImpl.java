package ru.sbt.examples.dinamic.cash.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceImpl implements IService {

    @Override
    public Double doHardWork(String param1, Integer param2) {
//Какая-то надуманная реализация реализация
        String value=""+LocalDateTime.now().getHour()+LocalDateTime.now().getMinute()+"."+param2;
        return Double.valueOf(value);
    }


    @Override
    public List<String> doListMoreLines() {
        List<String> resultList=new ArrayList<>();
        for(int i=0;i<100000;i++){
            resultList.add("List-"+i);
        }

        return resultList;
    }
}
