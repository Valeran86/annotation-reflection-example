package ru.sbt.examples.cachedProxy.cachedService;


import ru.sbt.examples.cachedProxy.cache.*;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;

import static ru.sbt.examples.cachedProxy.cachedService.CachedServiceFileUtils.*;


public class CachedServiceHandler implements InvocationHandler {
    private Object cachedObject;
    private static Map<List<Object>, Object> cache = new HashMap<>();

    public CachedServiceHandler( Object object ) {
        this.cachedObject = object;
    }

    @Override
    public Object invoke( Object proxy, Method method, Object[] args ) throws Throwable {
        if ( !method.isAnnotationPresent( Cache.class ) )
            return method.invoke( cachedObject, args );

        Cache annotation = method.getAnnotation( Cache.class );
        String dirDataFiles = annotation.dirDataFiles();
        String fileNamePrefix = annotation.fileNamePrefix();
        boolean zip = annotation.zip();
        int listCount = annotation.listCount();

        List<Object> key = getKey( method, args );
        Object value = null;
        switch ( annotation.cacheType() ) {
            case IN_MEMORY:
                System.out.print( "MEMORY " );
                if ( !cache.containsKey( key ) ) {
                    System.out.print( "calc " );
                    value = method.invoke( cachedObject, args );
                    cache.put( key, value );

                } else {
                    System.out.print( "cache " );
                    value = cache.get( key );
                }
                break;
            case FILE:
                System.out.print( "FILE " );


                File dir = new File( dirDataFiles );
                File filename = getFilename( dir, fileNamePrefix, key.hashCode() );
                if ( filename.exists() ) {
                    System.out.print( "cache " );
                    value = deserialize( filename, zip );
                } else {
                    System.out.print( "calc " );
                    value = method.invoke( cachedObject, args );
                    serialize( filename, value, zip );
                }
                break;
            default:
                value = method.invoke( cachedObject, args );
                break;
        }

        if ( method.getReturnType().isAssignableFrom( List.class ) && listCount > 0 )
            value = ( (List) value ).subList( 0, listCount );
        return value;
    }

    private List<Object> getKey( Method method, Object[] args ) {
        List<Object> key = new ArrayList<>();
        if ( args == null ) return key;
        //Александр, вот эту инициализацию можно сделать как то поудачнее, например при помощи Stream?
        for ( int i = 0; i < args.length; i++ ) {
            if ( !method.getParameters()[i].isAnnotationPresent( CachIdIgnore.class ) )
                key.add( args[i] );
        }

        if ( key.size() == 0 )
            throw new IllegalArgumentException( "Incorrect usage @CachIdIgnore annotation: all parameters of method \""
                    + method.getName() + "\" are marked as @CachIdIgnore" );
        return key;
    }

}
