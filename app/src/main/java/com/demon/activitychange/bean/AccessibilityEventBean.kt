package com.demon.activitychange.bean

import java.io.Serializable

/**
 * Created by LIXH on 2019/4/23.
 * email lixhVip9@163.com
 * des
 */
data class AccessibilityEventBean(var packageName: CharSequence,
                                  var name: CharSequence,
                                  var text: List<CharSequence>) : Serializable
