package com.lixh.app

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import com.lixh.utils.Global
import java.util.*
import kotlin.system.exitProcess

/**
 * activity管理
 */
object AppManager {
    private var activityStack: Stack<Activity> = Stack()
    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity) {

        activityStack.add(activity)
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity? {
        return try {
            activityStack.lastElement()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    /**
     * 获取当前Activity的前一个Activity
     */
    fun preActivity(): Activity? {
        val index = activityStack.size - 2
        return if (index < 0) {
            null
        } else activityStack[index]
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        val activity = activityStack.lastElement()
        finishActivity(activity)
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        activity?.let {
            activityStack.remove(it)
            it.finish()
            null
        }
    }

    /**
     * 移除指定的Activity
     */
    fun removeActivity(activity: Activity?) {
        activity?.let {
            activityStack.remove(it)
            null
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        try {
            for (activity in this.activityStack) {
                if (activity.javaClass == cls) {
                    finishActivity(activity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        activityStack.forEach { activity ->
            activity.finish()
        }
        activityStack.clear()
    }

    /**
     * 返回到指定的activity
     *
     * @param cls
     */
    fun returnToActivity(cls: Class<*>) {
        while (activityStack.size != 0)
            if (activityStack.peek().javaClass == cls) {
                break
            } else {
                finishActivity(activityStack.peek())
            }
    }


    /**
     * 是否已经打开指定的activity
     *
     * @param cls
     * @return
     */
    fun isOpenActivity(cls: Class<*>): Boolean {
        var i = 0
        val size = activityStack.size
        while (i < size) {
            if (cls == activityStack.peek().javaClass) {
                return true
            }
            i++
        }
        return false
    }

    /**
     * 退出应用程序
     */
    fun notifyExitApp() {
        appExit(true)
    }

    /**
     * 退出应用程序
     *
     * @param isBackground 是否开开启后台运行
     */
    private fun appExit(isBackground: Boolean?) {
        try {
            finishAllActivity()
            val manager = BaseApplication.appContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            manager.killBackgroundProcesses(BaseApplication.appContext
                    .packageName)
        } catch (e: Exception) {

        } finally {
            // 注意，如果您有后台程序运行，请不要支持此句子
            if (isBackground!!) {
                exitProcess(0)
            }
        }
    }
}