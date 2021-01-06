package com.lixh.logger

/**
 * Logger is a wrapper of [android.util.Log]
 * But more pretty, simple and powerful
 *
 * @author Orhan Obut
 */
object Logger {

    private val printer = LoggerPrinter()
    private const val DEFAULT_METHOD_COUNT = 2
    private const val DEFAULT_TAG = "Logger"

    /**
     * It is used to get the settings object in order to change settings
     *
     * @return the settings object
     */
    fun init(): Settings {
        return printer.init(DEFAULT_TAG)
    }

    /**
     * It is used to change the tag
     *
     * @param tag is the given string which will be used in Logger
     */
    fun init(tag: String): Settings {
        return printer.init(tag)
    }

    fun t(tag: String): Printer {
        return printer.t(tag, DEFAULT_METHOD_COUNT)
    }

    fun t(methodCount: Int): Printer {
        return printer.t(null, methodCount)
    }

    fun t(tag: String, methodCount: Int): Printer {
        return printer.t(tag, methodCount)
    }

    fun d(message: String, vararg args: Any) {
        printer.d(message, *args)
    }

    fun e(message: String, vararg args: Any) {
        printer.e(null, message, *args)
    }

    fun e(throwable: Throwable, message: String, vararg args: Any) {
        printer.e(throwable, message, *args)
    }

    fun i(message: String, vararg args: Any) {
        printer.i(message, *args)
    }

    fun v(message: String, vararg args: Any) {
        printer.v(message, *args)
    }

    fun w(message: String, vararg args: Any) {
        printer.w(message, *args)
    }

    fun wtf(message: String, vararg args: Any) {
        printer.wtf(message, *args)
    }

    /**
     * Formats the json content and print it
     *
     * @param json the json content
     */
    fun json(json: String) {
        printer.json(json)
    }

    /**
     * Formats the json content and print it
     *
     * @param xml the xml content
     */
    fun xml(xml: String) {
        printer.xml(xml)
    }

}//no instance