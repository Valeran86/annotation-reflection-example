package ru.sbt.examples.cacheproxy;

import java.math.BigInteger;


public class ServiceImpl implements Service {

    private BigInteger result = null;
    private int value;

    @Cacheble (isCacheble = true)
    public BigInteger factorial( int value ) {

        this.value = value;
        BigInteger ret = BigInteger.ONE;
        for (int i = 1; i <= value; ++i) ret = ret.multiply(BigInteger.valueOf(i));
        result = ret;
        return ret;

    }

    @Override
    public String toString() {
        if ( (result == null) || (value == 0) ) return "Use ServiceImpl.factorial( value ) for calculate factorial!";
        return "Factorial for " + String.valueOf( this.value )  + " = " + result.toString();
    }

}
