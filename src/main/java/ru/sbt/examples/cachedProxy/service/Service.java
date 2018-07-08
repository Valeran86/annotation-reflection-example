package ru.sbt.examples.cachedProxy.service;

import ru.sbt.examples.cachedProxy.cache.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static ru.sbt.examples.cachedProxy.cache.CacheType.*;

public interface Service extends Serializable {
    @Cache( cacheType = FILE,
            fileNamePrefix = "dataCach",
            zip = true,
            listCount = 2 )
    List<String> run( String item, double value, float f );

    @Cache( cacheType = IN_MEMORY,
            listCount = 2 )
    List<String> work( String item );

    @Cache( cacheType = IN_MEMORY )
    Double doHardWork( @CachIdIgnore String item, int value );

    @Cache
    int test2( );
}
