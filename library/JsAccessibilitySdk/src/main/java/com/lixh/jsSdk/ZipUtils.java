package com.lixh.jsSdk;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public static void Unzip(InputStream inputStream, String savefilename) throws IOException {
        // 创建解压目标目录
        File file = new File(savefilename);
        // 如果目标目录不存在，则创建
        if (!file.exists()) {
            file.mkdirs();
        }

        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        // 读取一个进入点
        ZipEntry nextEntry = zipInputStream.getNextEntry();
        byte[] buffer = new byte[1024 * 1024];
        int count = 0;
        // 如果进入点为空说明已经遍历完所有压缩包中文件和目录
        while (nextEntry != null) {
            // 如果是一个文件夹
            if (nextEntry.isDirectory()) {
                file = new File(savefilename + File.separator + nextEntry.getName());
                if (!file.exists()) {
                    file.mkdir();
                }
            } else {
                // 如果是文件那就保存
                file = new File(savefilename + File.separator + nextEntry.getName());
                // 则解压文件
                if (!file.exists()) {
                    file.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file);
                    while ((count = zipInputStream.read(buffer)) != -1) {
                        fos.write(buffer, 0, count);
                    }

                    fos.close();
                }
            }

            //这里很关键循环解读下一个文件
            nextEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();

    }
}
