package com.lixh.logger

import android.util.Log
import com.lixh.logger.Settings.isShowThreadInfo
import com.lixh.logger.Settings.logLevel
import com.lixh.logger.Settings.methodCount
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.StringReader
import java.io.StringWriter
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

/**
 * Logger is a wrapper of [Log]
 * But more pretty, simple and powerful
 *
 * @author Orhan Obut
 */
internal class LoggerPrinter : Printer {

    override fun t(tag: String?, methodCount: Int): Printer {
        if (tag != null) {
            LOCAL_TAG.set(tag)
        }
        LOCAL_METHOD_COUNT.set(methodCount)
        return this
    }

    override fun init(tag: String): Settings {
        if (tag == null) {
            throw NullPointerException("tag may not be null")
        }
        if (tag.trim { it <= ' ' }.isEmpty()) {
            throw IllegalStateException("tag may not be empty")
        }
        TAG = tag
        return Settings
    }

    override fun d(message: String, vararg args: Any) {
        log(Log.DEBUG, message, *args)
    }

    override fun e(message: String, vararg args: Any) {
        e(null, message, *args)
    }

    override fun e(throwable: Throwable?, message: String, vararg args: Any) {
        var message = message
        throwable?.let {
            if (message != null) {
                message += " : $it"
            } else {
                message = throwable.toString()
            }
        }
        if (message == null) {
            message = "No message/exception is set"
        }
        log(Log.ERROR, message, *args)
    }

    override fun w(message: String, vararg args: Any) {
        log(Log.WARN, message, *args)
    }

    override fun i(message: String, vararg args: Any) {
        log(Log.INFO, message, *args)
    }

    override fun v(message: String, vararg args: Any) {
        log(Log.VERBOSE, message, *args)
    }

    override fun wtf(message: String, vararg args: Any) {
        log(Log.ASSERT, message, *args)
    }

    override fun json(json: String) {
        if (json.isEmpty()) {
            d("Empty/Null json content")
            return
        }
        try {
            if (json.startsWith("{")) {
                val jsonObject = JSONObject(json)
                val message = jsonObject.toString(JSON_INDENT)
                d(message)
                return
            }
            if (json.startsWith("[")) {
                val jsonArray = JSONArray(json)
                val message = jsonArray.toString(JSON_INDENT)
                d(message)
            }
        } catch (e: JSONException) {
            e(e.cause?.message + "\n" + json)
        }
    }

    override fun xml(xml: String) {
        if (xml.isEmpty()) {
            d("Empty/Null xml content")
            return
        }
        try {
            val xmlInput = StreamSource(StringReader(xml))
            val xmlOutput = StreamResult(StringWriter())
            val transformer = TransformerFactory.newInstance().newTransformer()
            transformer.setOutputProperty(OutputKeys.INDENT, "yes")
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
            transformer.transform(xmlInput, xmlOutput)
            d(xmlOutput.writer.toString().replaceFirst(">".toRegex(), ">\n"))
        } catch (e: TransformerException) {
            e(e.cause?.message + "\n" + xml)
        }
    }

    /**
     * This method is synchronized in order to avoid messy of logs' order.
     */
    @Synchronized
    private fun log(logType: Int, msg: String, vararg args: Any) {
        if (logLevel == LogLevel.NONE) {
            return
        }
        val tag = TAG
        val message = createMessage(msg, *args)
        val methodCount = methodCount

        logTopBorder(logType, tag)
        logHeaderContent(logType, tag, methodCount)

        //get bytes of message with system's default charset (which is UTF-8 for Android)
        val bytes = message.toByteArray()
        val length = bytes.size
        if (length <= CHUNK_SIZE) {
            if (methodCount > 0) {
                logDivider(logType, tag)
            }
            logContent(logType, tag, message)
            logBottomBorder(logType, tag)
            return
        }
        if (methodCount > 0) {
            logDivider(logType, tag)
        }
        var i = 0
        while (i < length) {
            val count = Math.min(length - i, CHUNK_SIZE)
            //create a new String with system's default charset (which is UTF-8 for Android)
            logContent(logType, tag, String(bytes, i, count))
            i += CHUNK_SIZE
        }
        logBottomBorder(logType, tag)
    }

    private fun logTopBorder(logType: Int, tag: String) {
        logChunk(logType, tag, TOP_BORDER)
    }

    private fun logHeaderContent(logType: Int, tag: String, methodCount: Int) {
        var methodCount = methodCount
        val trace = Thread.currentThread().stackTrace
        if (isShowThreadInfo) {
            logChunk(logType, tag, HORIZONTAL_DOUBLE_LINE + " Thread: " + Thread.currentThread().name)
            logDivider(logType, tag)
        }
        var level = ""

        val stackOffset = getStackOffset(trace)

        //corresponding method count with the current stack may exceeds the stack trace. Trims the count
        if (methodCount + stackOffset > trace.size) {
            methodCount = trace.size - stackOffset - 1
        }

        for (i in methodCount downTo 1) {
            val stackIndex = i + stackOffset
            if (stackIndex >= trace.size) {
                continue
            }
            val builder = StringBuilder()
            builder.append("║ ")
                    .append(level)
                    .append(getSimpleClassName(trace[stackIndex].className))
                    .append(".")
                    .append(trace[stackIndex].methodName)
                    .append(" ")
                    .append(" (")
                    .append(trace[stackIndex].fileName)
                    .append(":")
                    .append(trace[stackIndex].lineNumber)
                    .append(")")
            level += "   "
            logChunk(logType, tag, builder.toString())
        }
    }

    private fun logBottomBorder(logType: Int, tag: String) {
        logChunk(logType, tag, BOTTOM_BORDER)
    }

    private fun logDivider(logType: Int, tag: String) {
        logChunk(logType, tag, MIDDLE_BORDER)
    }

    private fun logContent(logType: Int, tag: String, chunk: String) {
        val lines = chunk.split(System.getProperty("line.separator").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (line in lines) {
            logChunk(logType, tag, "$HORIZONTAL_DOUBLE_LINE $line")
        }
    }

    private fun logChunk(logType: Int, tag: String, chunk: String) {
        val finalTag = formatTag(tag)
        when (logType) {
            Log.ERROR -> Log.e(finalTag, chunk)
            Log.INFO -> Log.i(finalTag, chunk)
            Log.VERBOSE -> Log.v(finalTag, chunk)
            Log.WARN -> Log.w(finalTag, chunk)
            Log.ASSERT -> Log.wtf(finalTag, chunk)
            Log.DEBUG -> Log.d(finalTag, chunk)
            // Fall through, log debug by default
            else -> Log.d(finalTag, chunk)
        }
    }

    private fun getSimpleClassName(name: String): String {
        val lastIndex = name.lastIndexOf(".")
        return name.substring(lastIndex + 1)
    }

    private fun formatTag(tag: String): String {
        return if (tag.isNotEmpty() && TAG !== tag) {
            "$TAG-$tag"
        } else TAG
    }

    private fun createMessage(message: String, vararg args: Any): String {
        return if (args.isEmpty()) message else String.format(message, *args)
    }

    /**
     * Determines the starting index of the stack trace, after method calls made by this class.
     *
     * @param trace the stack trace
     * @return the stack offset
     */
    private fun getStackOffset(trace: Array<StackTraceElement>): Int {
        var i = MIN_STACK_OFFSET
        while (i < trace.size) {
            val e = trace[i]
            val name = e.className
            if (name != LoggerPrinter::class.java.name && name != Logger::class.java.name) {
                return --i
            }
            i++
        }
        return -1
    }

    companion object {

        /**
         * Android's max limit for a log entry is ~4076 bytes,
         * so 4000 bytes is used as chunk size since default charset
         * is UTF-8
         */
        private val CHUNK_SIZE = 4000

        /**
         * It is used for json pretty print
         */
        private const val JSON_INDENT = 4

        /**
         * The minimum stack trace index, starts at this class after two native calls.
         */
        private const val MIN_STACK_OFFSET = 3

        /**
         * Drawing toolbox
         */
        private const val TOP_LEFT_CORNER = '╔'
        private const val BOTTOM_LEFT_CORNER = '╚'
        private const val MIDDLE_CORNER = '╟'
        private const val HORIZONTAL_DOUBLE_LINE = '║'
        private const val DOUBLE_DIVIDER = "════════════════════════════════════════════"
        private const val SINGLE_DIVIDER = "────────────────────────────────────────────"
        private val TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER
        private val BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER
        private val MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER

        /**
         * TAG is used for the Log, the name is a little different
         * in order to differentiate the logs easily with the filter
         */
        var TAG = "logger"

        /**
         * Localize single tag and method count for each thread
         */
        val LOCAL_TAG = ThreadLocal<String>()
        val LOCAL_METHOD_COUNT = ThreadLocal<Int>()
    }
}