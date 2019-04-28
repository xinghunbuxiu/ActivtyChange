package com.lixh.jsSdk;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by LIXH on 2019/4/28.
 * email lixhVip9@163.com
 * des
 */
public class ZipUtils {
    public static void Unzip(String zipFile, String targetDir) {
        int BUFFER = 4096; //这里缓冲区我们使用4KB，
        String strEntry; //保存每个zip的条目名称
        try {
            BufferedOutputStream dest = null; //缓冲输出流
            FileInputStream fis = new FileInputStream(zipFile);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry; //每个zip条目的实例
            while ((entry = zis.getNextEntry()) != null) {

                try {
                    Log.i("Unzip: ", "=" + entry);
                    int count;
                    byte data[] = new byte[BUFFER];
                    strEntry = entry.getName();

                    File entryFile = new File(targetDir + strEntry);
                    File entryDir = new File(entryFile.getParent());
                    if (!entryDir.exists()) {
                        entryDir.mkdirs();
                    }
                    FileOutputStream fos = new FileOutputStream(entryFile);
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            zis.close();
        } catch (Exception cwj) {
            cwj.printStackTrace();
        }
    }
}
