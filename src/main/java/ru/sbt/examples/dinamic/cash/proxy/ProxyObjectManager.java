package ru.sbt.examples.dinamic.cash.proxy;

import ru.sbt.examples.dinamic.cash.proxy.constants.Constants;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProxyObjectManager {
    private static List<ProxyObject> listProxyObject=new ArrayList<>();
    private static String fileNameTxt;


    public static ProxyObject newProxyObjectList(Method metod, HashMap<Class, Object> hashMapAttr, HashMap<String,Object> hashMapAttrAnnotation, Object returnValue){
        ProxyObject proxyObject= new ProxyObject(metod,hashMapAttr,hashMapAttrAnnotation,returnValue);
        listProxyObject.add(proxyObject);
        return proxyObject;
    }

    public static List<ProxyObject> allProxyObject(){
        List<ProxyObject> newListProxyObject=new ArrayList<>();
        newListProxyObject.addAll(listProxyObject);
        return  newListProxyObject;
    }

     public static void setFileName(String fileName){
        fileNameTxt=fileName+".txt";
    }
    public static ProxyObject proxyObjectContainsMemory(Method method, HashMap<Class, Object> hashMapAttr,
                                                  HashMap<String,Object> hashMapAttrAnnotation){
        ProxyObject newProxyObject= new ProxyObject(method,hashMapAttr,hashMapAttrAnnotation,null);
        for(ProxyObject proxyObject:listProxyObject){
            if(newProxyObject.equals(proxyObject)){
                return proxyObject;
            }
        }
        return  newProxyObject;
    }

    public static ProxyObject proxyObjectContainsFile(Method method, HashMap<Class, Object> hashMapAttr,
                                          HashMap<String,Object> hashMapAttrAnnotation){
        List<ProxyObject> proxyObjectsList=returnProxyObjectsFile();
        ProxyObject newProxyObject= new ProxyObject(method,hashMapAttr,hashMapAttrAnnotation,null);
        for(ProxyObject proxyObject:proxyObjectsList){
            if(newProxyObject.equals(proxyObject)){
                return proxyObject;
            }
        }
        return  newProxyObject;
    }




    public static void addReturnValueInProxyObject(ProxyObject proxyObject,Object returnValue, String typeCache){
        proxyObject.setReturnValue(returnValue);
        switch (typeCache){
            case Constants.IN_MEMORY:
                listProxyObject.add(proxyObject);
                break;
            case Constants.IN_FILE:
                List<ProxyObject> proxyObjectsList=returnProxyObjectsFile();
                proxyObjectsList.add(proxyObject);
                writeProxyObjectsFile(proxyObjectsList);
                break;
            default:
                throw new IllegalArgumentException("ERROR: Отсутствует тип кэширования. Возможно в аннотации " +
                        "CacheDynamicProxy установлен тип кэширования, обработка которого отсутствует.");
        }
    }

    private static void writeProxyObjectsFile(List<ProxyObject> proxyObjectsList){
        try {
            FileOutputStream fos = new FileOutputStream(fileNameTxt);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for (ProxyObject proxyObject:proxyObjectsList){
                oos.writeObject(proxyObject);
            }
            oos.flush();
            oos.close();
        }catch (java.io.NotSerializableException nse){
            System.out.println("Error:Объект запиываемый в файл не Серрриализован. Произведите сериализацию объекта " +
                    "путем добавления implements Serializable: "+ nse.getMessage());
        }
        catch (IOException ex){
            ex.printStackTrace(System.out);
        }
    }

    private static List<ProxyObject> returnProxyObjectsFile(){
        List<ProxyObject> proxyObjectsList=new ArrayList<>();
        if(!(new File(fileNameTxt)).exists()){
            return proxyObjectsList;
        }
        try{ FileInputStream fis = new FileInputStream(fileNameTxt);
            ObjectInputStream inputStream = new ObjectInputStream(fis);
            while (fis.available() > 0) {
                ProxyObject proxyObject=(ProxyObject) inputStream .readObject();
                proxyObjectsList.add(proxyObject);
            }
            inputStream.close();
        }
        catch(IOException e){
            System.out.println("Error:Объект запиываемый в файл не Серрриализован. Произведите сериализацию объекта " +
                    "путем добавления implements Serializable: "+ e.getMessage());
        }
        catch(Exception e){
            System.out.println("Error: Ошибка при чтении файла. Убедитесь что файл присуствует и доступен для чтения. "
                    +e.getMessage());
        }
        return proxyObjectsList;
    }


}
