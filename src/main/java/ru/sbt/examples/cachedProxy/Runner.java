package ru.sbt.examples.cachedProxy;

import ru.sbt.examples.cachedProxy.cachedService.CachedServiceFactory;
import ru.sbt.examples.cachedProxy.service.Service;
import ru.sbt.examples.cachedProxy.service.ServiceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.System.exit;


public class Runner {
    public static void main( String[] args ) {
        ServiceImpl impl = new ServiceImpl();
        Service cachedService = (Service) CachedServiceFactory.newInstance( impl );
        Runner runner = new Runner();
        runner.run( cachedService );
    }

    public void run( Service service ) {
        double r1 = service.doHardWork( "work1", 10 ); //считает результат
        System.out.println( r1 );
        double r2 = service.doHardWork( "work2", 5 );  //считает результат
        System.out.println( r2 );
        double r3 = service.doHardWork( "work1", 10 ); //результат из кеша
        System.out.println( r3 );

        System.out.println( service.doHardWork( "244133123234", 1 ) );
        System.out.println( service.doHardWork( "244133123234", 2 ) );
        System.out.println( service.doHardWork( "1", 3 ) );
        System.out.println( service.doHardWork( "2", 2 ) );
        System.out.println( service.doHardWork( "2", 3 ) );
        System.out.println( service.doHardWork( "1", 3 ) );
        System.out.println( service.run( "1", 3.3, 3.6f ) );
        System.out.println( service.run( "1", 3.3, 3.6f ) );
        System.out.println( service.test2() );
        System.out.println( service.test2() );
        System.out.println( service.test2() );
    }
}
