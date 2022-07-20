package fr.robotv2.opixelwarp.util;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    public static void setupFile(File file) {
        if(!file.exists()) {
            if(!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
