package ru.sbt.examples.cachedProxy.cache;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static ru.sbt.examples.cachedProxy.cache.CacheType.*;

@Retention( value = RetentionPolicy.RUNTIME )
public @interface Cache {
    CacheType cacheType( ) default IN_MEMORY;

    String fileNamePrefix( ) default "cachedData";

    String dirDataFiles( ) default "/datafiles/";

    boolean zip( ) default false;

    int listCount( ) default 0;
}
