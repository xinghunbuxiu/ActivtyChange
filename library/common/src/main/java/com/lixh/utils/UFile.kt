package com.lixh.utils

import android.net.Uri
import android.os.Environment
import com.lixh.app.BaseApplication
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.URI

/**
 * 文件处理util
 */
object UFile {
    private const val EOF = -1
    private const val DEFAULT_BUFFER_SIZE = 1024 * 4

    /***
     * 获取项目文件
     *
     * @return
     */
    val dir: File?
        get() {
            val packName = BaseApplication.appContext.packageName
            var dir: File = when (Environment.getExternalStorageState()) {
                Environment.MEDIA_MOUNTED -> BaseApplication.appContext.cacheDir
                else -> {
                    File(Environment.getExternalStorageDirectory()
                            .absolutePath + "/" + packName)
                }
            }
            return dir.apply { mkdirs() }
        }


    /**
     * 获取项目缓存文件
     *
     * @return
     */
    val cacheDir: String
        get() {
            val file = File(dir?.absolutePath + "/cache")
            if (!file.exists()) {
                file.mkdirs()
            }
            return file.absolutePath
        }

    /**
     * 获取项目使用过程中产生的图片文件
     *
     * @return
     */
    val imageDir: String
        get() {
            val file = File(dir?.absolutePath + "/image")
            file.mkdirs()
            return file.absolutePath
        }

    /**
     * 是否存在SDcard
     *
     * @return
     */
    val isExistsSdcard: Boolean
        get() = Environment.MEDIA_MOUNTED == Environment
                .getExternalStorageState()

    /**
     * 获取项目使用的数据库
     *
     * @return
     */
    val dbDir: String
        get() {
            val file = File(dir?.absolutePath + "/db")
            file.mkdirs()
            return file.absolutePath
        }


    /**
     * 获取文件名
     *
     * @param fileName
     * @return
     */
    internal fun splitFileName(fileName: String): Array<String> {
        var name = fileName
        var extension = ""
        val i = fileName.lastIndexOf(".")
        if (i != -1) {
            name = fileName.substring(0, i)
            extension = fileName.substring(i)
        }

        return arrayOf(name, extension)
    }

    /**
     * 根据uri获取文件路径
     *
     * @param context
     * @param uri
     * @return
     */
    internal fun getFileName(uri: URI): String {
        var result: String? = File(uri).name ?: uri.path
        val cut = result!!.lastIndexOf(File.separator)
        if (cut != -1) {
            result = result.substring(cut + 1)
        }
        return result
    }

    /**
     * 根据uri获取真文件路径
     *
     * @param uri
     * @return
     */
    internal fun getRealPathFromURI(uri: Uri): String? {

        return File(URI(uri.toString())).name ?: uri.path

    }

    @Throws(IOException::class)
    internal fun copy(input: InputStream, output: OutputStream?): Int {
        val count = copyLarge(input, output)
        return if (count > Integer.MAX_VALUE) {
            -1
        } else count.toInt()
    }

    @Throws(IOException::class)
    @JvmOverloads
    internal fun copyLarge(input: InputStream, output: OutputStream?, buffer: ByteArray = ByteArray(DEFAULT_BUFFER_SIZE)): Long {
        var count: Long = 0
        var read: Int
        input.use { input ->
            output.use { out ->
                while (input.read().also { n ->
                            read = n
                        } != EOF) {
                    out?.write(buffer, 0, read)
                }
            }
        }
        return count
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath
     * @return
     */
    fun isExistsFile(filePath: String): Boolean {
        if (isExistsSdcard) {// 判断SDcard是否存在
            return File(filePath)?.exists()
        }
        return false
    }


    /**
     * 读文
     *
     * @param file
     * @return
     */
    fun read(file: File): String {
        if (!file.exists()) {
            return ""
        }
        return File(file.path).readText()
    }


}
