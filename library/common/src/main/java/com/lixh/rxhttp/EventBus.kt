/*
 * Copyright (c) 1994, 2004, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.lixh.rxhttp


import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.lixh.annotation.EventRegister
import com.lixh.app.BaseApplication
import java.lang.reflect.Method
import java.util.*
import kotlin.system.exitProcess

//异步分发 事件
open class EventBus : LifecycleObserver {
    private val obs: MutableMap<LifecycleOwner, Map<String, Method>> = HashMap()
    private var activityStack: Stack<Activity> = Stack()

    /**
     * Adds an observer to the set of observers for this object, provided
     * that it is not the same as some observer already in the set.
     * The order in which notifications will be delivered to multiple
     * observers is not specified. See the class comment.
     *
     * @param o an observer to be added.
     * @throws NullPointerException if the parameter o is null.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    @Synchronized
    fun addObserver(
            base: LifecycleOwner) {
        val annotations = base.javaClass.declaredMethods
        annotations.filter {
            it.getAnnotation(EventRegister::class.java) != null
        }.associateBy {
            it.getAnnotation(EventRegister::class.java).key
        }.apply {
            obs[base] = this
        }
        if (base is Activity) {
            activityStack.add(base)
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    @Synchronized
    fun deleteObserver(base: LifecycleOwner) {
        obs.remove(base)
        if (base is Activity) {
            activityStack.remove(base)
            base.lifecycle.removeObserver(this)
            base.finish()
        }
    }

    //通知 key 注册的 和参数 k to v
    @Override
    open fun notifyObservers(key: String, vararg param: Pair<String, Any?>) {
        // 是否注册有 key
        obs.keys.takeWhile { obs[it]!!.containsKey(key) }.forEach { view ->
            obs[view].takeIf { !it.isNullOrEmpty() }.apply {
                this?.get(key)?.apply {
                    invoke(view, param)
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    private fun finishAllActivity() {
        activityStack.forEach { activity ->
            activity.finish()
        }
        activityStack.clear()
    }

    /**
     * 退出应用程序
     *
     * @param isBackground 是否开开启后台运行
     */
    fun appExit(isBackground: Boolean) {
        try {
            finishAllActivity()
            val manager = BaseApplication.appContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            manager.killBackgroundProcesses(BaseApplication.appContext
                    .packageName)
        } catch (e: Exception) {

        } finally {
            // 注意，如果您有后台程序运行，请不要支持此句子
            if (isBackground) {
                exitProcess(0)
            }
        }
    }


}
