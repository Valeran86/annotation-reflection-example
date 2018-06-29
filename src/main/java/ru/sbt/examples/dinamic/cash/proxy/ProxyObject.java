package ru.sbt.examples.dinamic.cash.proxy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Objects;

public class ProxyObject {
    private Method metod;

    private HashMap<Class,Object> hashMapAttr;

    private HashMap<String,Object> hashMapAttrAnnotation;

    private Object returnValue;

    public ProxyObject(Method metod, HashMap<Class, Object> hashMapAttr, HashMap<String,Object> hashMapAttrAnnotation,Object returnValue) {
        this.metod = metod;
        this.hashMapAttr = hashMapAttr;
        this.hashMapAttrAnnotation = hashMapAttrAnnotation;
        this.returnValue=returnValue;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    public Method getMetod() {
        return metod;
    }

    public void setMetod(Method metod) {
        this.metod = metod;
    }

    public HashMap<Class, Object> getHashMapAttr() {
        return hashMapAttr;
    }

    public void setHashMapAttr(HashMap<Class, Object> hashMapAttr) {
        this.hashMapAttr = hashMapAttr;
    }

    public HashMap<String, Object> getHashMapAttrAnnotation() {
        return hashMapAttrAnnotation;
    }

    public void setHashMapAttrAnnotation(HashMap<String, Object> hashMapAttrAnnotation) {
        this.hashMapAttrAnnotation = hashMapAttrAnnotation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProxyObject that = (ProxyObject) o;
        return Objects.equals(metod, that.metod) &&
                Objects.equals(hashMapAttr, that.hashMapAttr) &&
                Objects.equals(hashMapAttrAnnotation, that.hashMapAttrAnnotation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metod, hashMapAttr, hashMapAttrAnnotation);
    }
}
