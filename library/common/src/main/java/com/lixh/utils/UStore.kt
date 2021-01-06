/**
 * Copyright 2016 JustWayward Team
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lixh.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.lixh.app.BaseApplication
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by lfh on 2016/8/13.
 */
class UStore<T>(
        private val key: String,
        private val defaultValue: T,
        private val useCommit: Boolean = false
) : ReadWriteProperty<Any?, T> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getSharePreferences(key, defaultValue)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putSharePreferences(key, value)
    }

    //延迟属性，只会加载一次
    private val prefs: SharedPreferences by lazy {
        BaseApplication.instance.getSharedPreferences(
                BaseApplication.instance.packageName,
                Context.MODE_PRIVATE
        )
    }

    @SuppressLint("ApplySharedPref")
    private fun putSharePreferences(name: String, value: T) = with(prefs.edit()) {
        val editor = when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("Type Error, cannot be saved!")
        }
        if (useCommit) editor.commit() else editor.apply()
    }

    @Suppress("UNCHECKED_CAST")
    private fun getSharePreferences(name: String, default: T): T = with(prefs) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("Type Error, cannot be got!")
        }
        return res as T
    }
}
