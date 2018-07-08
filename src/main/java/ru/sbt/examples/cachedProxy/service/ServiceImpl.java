package ru.sbt.examples.cachedProxy.service;


import java.io.Serializable;
import java.util.*;

public class ServiceImpl implements Service, Serializable {
    @Override
    public List<String> run( String item, double value, float f ) {
        List<String> result = new ArrayList<>();
        for ( int i = 0; i < (int) ( value < 0 ? 0 : value ); i++ ) {
            result.add( item + ": " + f );
        }
        return result;
    }

    @Override
    public List<String> work( String item ) {
        return Arrays.asList( item.split( "-" ) );
    }

    @Override
    public Double doHardWork( String item, int value ) {
        return (double) item.length() / value;
    }

    @Override
    public int test2( ) {
        return 0;
    }
}
