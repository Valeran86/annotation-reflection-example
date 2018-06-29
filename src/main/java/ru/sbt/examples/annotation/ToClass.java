package ru.sbt.examples.annotation;

public class ToClass {
    private int value = 0;

    public void setValue( int value ) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Integer.toString( value );
    }
}
