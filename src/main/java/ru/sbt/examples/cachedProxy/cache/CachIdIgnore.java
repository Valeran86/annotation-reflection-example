package ru.sbt.examples.cachedProxy.cache;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention( value = RetentionPolicy.RUNTIME )
public @interface CachIdIgnore {
}
