package com.lixh.utils

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * author: rsw
 * created on: 2018/10/24 上午11:55
 * description:序列化工具类，可用于序列化对象到文件或从文件反序列化对象
 */

object SerializeUtils {
    /**
     * 从文件反序列化对象
     *
     * @param tag
     * @return
     * @throws RuntimeException if an error occurs
     */

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun deserializationSync(context: Context, tag: String, delete: Boolean): Any? {
        try {
            context.openFileInput(tag).use { fileInputStream ->
                ObjectInputStream(fileInputStream).use { objectInputStream ->
                    val `object` = objectInputStream.readObject()
                    if (delete) {
                        context.deleteFile(tag)
                    }
                    return `object`

                }
            }
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }


    //同步序列化
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun serializationSync(context: Context, tag: String, obj: Any?) {
        if (null == obj) {
            return
        }

        try {
            context.openFileOutput(tag, Context.MODE_PRIVATE).use { fileOutputStream -> ObjectOutputStream(fileOutputStream).use { objectOutputStream -> objectOutputStream.writeObject(obj) } }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}