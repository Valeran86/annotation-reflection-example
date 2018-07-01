package ru.sbt.examples.dinamic.cash.proxy;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Objects;

public class ProxyObject implements Serializable {
    private String methodName;

    transient private Method method;

    private HashMap<Class,Object> hashMapAttr;

    private HashMap<String,Object> hashMapAttrAnnotation;

    private Object returnValue;

    public ProxyObject(Method method, HashMap<Class, Object> hashMapAttr, HashMap<String,Object> hashMapAttrAnnotation,Object returnValue) {
        this.methodName=method.getName();
        this.method = method;
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

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.methodName=method.getName();
        this.method = method;
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
        return Objects.equals(methodName, that.methodName) &&
                Objects.equals(hashMapAttr, that.hashMapAttr) &&
                Objects.equals(hashMapAttrAnnotation, that.hashMapAttrAnnotation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(methodName, hashMapAttr, hashMapAttrAnnotation);
    }
}
