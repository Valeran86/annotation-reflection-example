package ru.sbt.examples.cachedProxy.cachedService;

import java.io.*;

public class CachedServiceFileUtils {

    public static File getFilename( File dir, String fileNamePrefix, int hash ) {
        if ( !dir.exists() ) dir.mkdir();
        File[] files = dir.listFiles( ( dir1, name )
                -> name.matches( "^" + fileNamePrefix + "\\." + hash + "\\.dat$" ) );

        if ( files == null || files.length != 1 )
            return new File( dir, fileNamePrefix + "." + hash + ".dat" );
        return files[0];
    }

    public static void serialize( File file, Object value, boolean zip ) {
        try (
                FileOutputStream fos = new FileOutputStream( file );
                ObjectOutputStream oos = new ObjectOutputStream( fos ) ) {
            oos.writeObject( value );
        } catch ( NullPointerException | IOException e ) {
            e.printStackTrace( System.out );
        }
    }

    public static Object deserialize( File file, boolean zip ) {
        Object ret = null;
        try (
                FileInputStream fis = new FileInputStream( file );
                ObjectInputStream ois = new ObjectInputStream( fis ) ) {
            ret = ois.readObject();
        } catch ( IOException | ClassNotFoundException e ) {
            e.printStackTrace( System.out );
        }
        return ret;
    }

}
