package ru.sbt.examples.dinamic.cash.proxy.manager;

import ru.sbt.examples.dinamic.cash.proxy.ProxyObject;
import ru.sbt.examples.dinamic.cash.proxy.constants.Constants;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileProxyObjectManager implements IProxyObjectManager {

    private static String fileNameTxt;

    public static void setFileName(String fileName){
        fileNameTxt=fileName+".txt";
    }

    @Override
    public void initialConfiguration(HashMap<String,Object> hashMapAttrAnnotation){
        fileNameTxt= hashMapAttrAnnotation.get(Constants.FILE_NAME)+".txt";
    }

    @Override
    public ProxyObject proxyObjectContains(Method method, HashMap<Class, Object> hashMapAttr,
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

    private static void writeProxyObjectsFile(List<ProxyObject> proxyObjectsList){
        FileOutputStream fos=null;
        ObjectOutputStream oos=null;
        try {
            fos=new FileOutputStream(fileNameTxt);
            oos = new ObjectOutputStream(fos);
            for (ProxyObject proxyObject:proxyObjectsList){
                oos.writeObject(proxyObject);
            }
            oos.flush();
        }catch (java.io.NotSerializableException nse){
            System.out.println("Error:Объект записываемый в файл не Серрриализован. Произведите сериализацию объекта " +
                    "путем добавления implements Serializable: "+ nse.getMessage());
        }
        catch (IOException ex){
            ex.printStackTrace(System.out);
        }finally {
            try{
                if(oos!=null){oos.close();}
                if(fos!=null){fos.close();}
            }catch (IOException ex){
                ex.printStackTrace(System.out);
            }
        }
    }

    @Override
    public void addReturnValueInProxyObject(ProxyObject proxyObject,Object returnValue){
        proxyObject.setReturnValue(returnValue);
        List<ProxyObject> proxyObjectsList=returnProxyObjectsFile();
        proxyObjectsList.add(proxyObject);
        writeProxyObjectsFile(proxyObjectsList);
    }
}
