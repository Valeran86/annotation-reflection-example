package ru.sbt.examples.reflection;

import java.util.Date;

public class TestClass {
    private int i;
    private static String s;
    private Date dddddDdDdDdD;
    private long LoNg;
    private boolean b;

    public void setup( ) {

    }

    public void setFakeI( int i ) {
        this.i = i;
    }

    public void setI( int i ) {
        this.i = i;
    }

    public static String getS( ) {
        return s;
    }

    public static void setS( String s ) {
        TestClass.s = s;
    }

    public Date getDddddDdDdDdD( ) {
        return dddddDdDdDdD;
    }

    public void setDddddDdDdDdD( Date dddddDdDdDdD ) {
        this.dddddDdDdDdD = dddddDdDdDdD;
    }

    public long getLoNg( ) {
        return LoNg;
    }

    public void setLoNg( long loNg ) {
        LoNg = loNg;
    }

    public boolean isB() {
        return b;
    }

    public void setB(boolean b) {
        this.b = b;
    }
}
