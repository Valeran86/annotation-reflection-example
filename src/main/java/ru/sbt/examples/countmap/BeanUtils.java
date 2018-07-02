package ru.sbt.examples.countmap;

import ru.sbt.collections.CountMapImpl;
import ru.sbt.examples.reflection.ReflectionExample;
import ru.sbt.examples.reflection.TestClass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;

public class BeanUtils {
    public static void main( String[] args ) {
        CountMapImpl< String > from = new CountMapImpl<>();
        CountMapImpl< String > to = new CountMapImpl<>();
        from.setField1( "field1_from" );
        from.setMap( new HashMap<>() );

        to.setField1( "field_to" );
        to.setMap( new HashMap<>() );
        assign( from, to );

        TestClass testClass1=new TestClass( 1, new Date(  ), 1l,true);
        TestClass testClass2=new TestClass( 2, new Date(  ), 2l,false);
        System.out.println(testClass1+" "+testClass2);
        assign( testClass1, testClass2 );
        System.out.println(testClass1+" "+testClass2);
    }

    public static void assign( Object from, Object to ) {
        ReflectionExample re = new ReflectionExample();

        for ( Method getter : re.getGetters( from.getClass() ) ) {
            for ( Method setter : re.getSetters( to.getClass() ) ) {
                if ( !methodsIsCorrespondents( getter, setter ) ) continue;
                try {
                    setter.invoke( to, getter.invoke( from ) );
                } catch ( IllegalAccessException | InvocationTargetException e ) {
                    e.printStackTrace( System.out );
                }
            }
        }
    }

    public static boolean methodsIsCorrespondents( Method getter, Method setter ) {
        String getterRoot = getter.getName().replaceFirst( "^(is|get)", "" );
        String setterRoot = setter.getName().replaceFirst( "^set", "" );
        return setter.getParameters()[0].getType().isAssignableFrom( getter.getReturnType() )
                && getterRoot.equals( setterRoot );
    }
}
