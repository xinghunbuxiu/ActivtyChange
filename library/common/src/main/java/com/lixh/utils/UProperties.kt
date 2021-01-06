package com.lixh.utils

import android.content.Context

import com.lixh.app.BaseApplication

import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.util.Properties

/**
 * 配置文件工具类
 *
 * @author gdpancheng@gmail.com 2013-10-22 下午1:08:52
 */
object UProperties {
    val TAG = "Tools_Properties"

    /**
     * 根据文件名和文件路径 读取Properties文件
     *
     * @param fileName
     * @param dirName
     * @return 设定文件
     */
    fun loadProperties(fileName: String, dirName: String): Properties {
        val props = Properties()
        try {
            val id = BaseApplication.appResources
                    .getIdentifier(fileName, dirName,
                            BaseApplication.appContext.getPackageName())
            props.load(BaseApplication.appContext.getResources()
                    .openRawResource(id))
        } catch (e: Exception) {
            ULog.e(TAG, e.toString())
        }

        return props
    }

    /**
     * 读取Properties文件(指定目录)
     *
     * @param file
     * @return 设定文件
     */
    fun loadConfig(file: String): Properties {
        val properties = Properties()
        try {
            val s = FileInputStream(file)
            properties.load(s)
        } catch (e: Exception) {
            ULog.e(TAG, e.toString())
        }

        return properties
    }

    /**
     * 保存Properties(指定目录)
     *
     * @param file
     * @param properties
     * 设定文件
     */
    fun saveConfig(file: String, properties: Properties) {
        try {
            val s = FileOutputStream(file, false)
            properties.store(s, "")
        } catch (e: Exception) {
            ULog.e(TAG, e.toString())
        }

    }

    /**
     * 读取文件 文件在/data/data/package_name/files下 无法指定位置
     * @param fileName
     * @return 设定文件
     */
    fun loadConfigNoDirs(fileName: String): Properties {
        val properties = Properties()
        try {
            val s = BaseApplication.appContext.openFileInput(
                    fileName)
            properties.load(s)
        } catch (e: Exception) {
            ULog.e(TAG, e.toString())
        }

        return properties
    }

    /**
     * 保存文件到/data/data/package_name/files下 无法指定位置
     *
     * @param fileName
     * @param properties
     * 设定文件
     */
    fun saveConfigNoDirs(fileName: String, properties: Properties) {
        try {
            val s = BaseApplication.appContext.openFileOutput(
                    fileName, Context.MODE_PRIVATE)
            properties.store(s, "")
        } catch (e: Exception) {
            ULog.e(TAG, e.toString())

        }

    }

    fun loadConfigAssets(fileName: String): Properties {

        val properties = Properties()
        try {
            val `is` = BaseApplication.appContext.getAssets()
                    .open(fileName)
            properties.load(`is`)
        } catch (e: Exception) {
            ULog.e(TAG, e.toString())
        }

        return properties
    }
}
