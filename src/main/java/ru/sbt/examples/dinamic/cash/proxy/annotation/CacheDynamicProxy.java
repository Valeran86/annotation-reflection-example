package ru.sbt.examples.dinamic.cash.proxy.annotation;

import ru.sbt.examples.dinamic.cash.proxy.constants.Constants;

import java.lang.annotation.*;

@Target(value=ElementType.METHOD)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface CacheDynamicProxy {
    String saveCacheType() default Constants.IN_MEMORY;
    int countList() default -1;
    boolean archivingZip() default false;
    Class[] identityBy() default {};
    String fileName() default "";
}
