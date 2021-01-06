package com.lixh.utils

import android.os.Handler
import android.os.HandlerThread

/**
 * 退出
 */
class Exit {
    var isExit = false

    private val task = Runnable { isExit = false }

    fun doExitInOneSecond() {

        isExit = true

        val thread = HandlerThread("doTask")

        thread.start()

        Handler(thread.looper).postDelayed(task, 2000)

    }

}
