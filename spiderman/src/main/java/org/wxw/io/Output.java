package org.wxw.io;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author wxw
 */
public class Output {
    public static boolean saveFileByBate(String savePath, String fileName, byte[] bytes) {

        File file = new File(savePath);
        if(!file.exists()){
            file.mkdirs();
        }

        file = new File(savePath+File.separator+fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }
}
