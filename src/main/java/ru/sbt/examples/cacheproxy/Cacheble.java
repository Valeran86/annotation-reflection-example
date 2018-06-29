package ru.sbt.examples.cacheproxy;

public @interface Cacheble {

    boolean isCacheble() default false;

}
