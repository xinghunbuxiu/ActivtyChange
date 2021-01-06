/*
 * Copyright 2012 GitHub Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lixh.utils

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.widget.TextView
import java.util.*
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Helpers for dealing with custom typefaces and measuring text to display
 */
object UTypeface {

    /**
     * Private repository icon
     */
    val ICON_PRIVATE = "\uf26a"

    /**
     * Public repository icon
     */
    val ICON_PUBLIC = "\uf201"

    /**
     * Fork icon
     */
    val ICON_FORK = "\uf202"

    /**
     * Create icon
     */
    val ICON_CREATE = "\uf203"

    /**
     * Delete icon
     */
    val ICON_DELETE = "\uf204"

    /**
     * Push icon
     */
    val ICON_PUSH = "\uf205"

    /**
     * Wiki icon
     */
    val ICON_WIKI = "\uf207"

    /**
     * Upload icon
     */
    val ICON_UPLOAD = "\uf20C"

    /**
     * Gist icon
     */
    val ICON_GIST = "\uf20E"

    /**
     * Add member icon
     */
    val ICON_ADD_MEMBER = "\uf21A"

    /**
     * Public mirror repository icon
     */
    val ICON_MIRROR_PUBLIC = "\uf224"

    /**
     * Public mirror repository icon
     */
    val ICON_MIRROR_PRIVATE = "\uf225"

    /**
     * Follow icon
     */
    val ICON_FOLLOW = "\uf21C"

    /**
     * Star icon
     */
    val ICON_STAR = "\uf02A"

    /**
     * Pull request icon
     */
    val ICON_PULL_REQUEST = "\uf222"

    /**
     * Issue open icon
     */
    val ICON_ISSUE_OPEN = "\uf226"

    /**
     * Issue reopen icon
     */
    val ICON_ISSUE_REOPEN = "\uf227"

    /**
     * Issue close icon
     */
    val ICON_ISSUE_CLOSE = "\uf228"

    /**
     * Issue comment icon
     */
    val ICON_ISSUE_COMMENT = "\uf229"

    /**
     * Comment icon
     */
    val ICON_COMMENT = "\uf22b"

    /**
     * News icon
     */
    val ICON_NEWS = "\uf234"

    /**
     * Watch icon
     */
    val ICON_WATCH = "\uf04e"

    /**
     * Team icon
     */
    val ICON_TEAM = "\uf019"

    /**
     * Code icon
     */
    val ICON_CODE = "\uf010"

    /**
     * Commit icon
     */
    val ICON_COMMIT = "\uf01f"

    /**
     * Person icon
     */
    val ICON_PERSON = "\uf218"

    /**
     * Add icon
     */
    val ICON_ADD = "\uf05d"

    /**
     * Broadcast icon
     */
    val ICON_BROADCAST = "\uf030"

    /**
     * Edit icon
     */
    val ICON_EDIT = "\uf058"

    private var OCTICONS: Typeface? = null

    /**
     * Find the maximum number of digits in the given numbers
     *
     * @param numbers
     * @return max digits
     */
    fun getMaxDigits(vararg numbers: Int): Int {
        var max = 1
        for (number in numbers)
            max = max(max, log10(number.toDouble()).toInt() + 1)
        return max
    }

    /**
     * Get width of number of digits
     *
     * @param view
     * @param numberOfDigits
     * @return number width
     */
    fun getWidth(view: TextView, numberOfDigits: Int): Int {
        val paint = Paint()
        paint.typeface = view.typeface
        paint.textSize = view.textSize
        val text = CharArray(numberOfDigits)
        Arrays.fill(text, '0')
        return paint.measureText(text, 0, text.size).roundToInt()
    }

    /**
     * Get typeface with name
     *
     * @param context
     * @param name
     * @return typeface
     */
    fun getTypeface(context: Context, name: String): Typeface {
        return Typeface.createFromAsset(context.assets, name)
    }
}
