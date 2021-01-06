/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.demon.activitychange.ui.api

object ApiConstants {
    val BASE_DUOKAN_URL = "http://www.duokan.com"
    val BASE_DUOKAN_INFO_URL = "https://cdn.cnbj1.fds.api.mi-img.com/"


    val BAIDU_URL = "http://app.video.baidu.com"

    val M_BAIDU_URL = "https://m.baidu.com"

    val BASE_URL = "http://www.wdfgm.com"

    val JUHE_URL = "http://v.juhe.cn"
    private val M_MAHUA_URL = "http://api.hbzjmf.com"

    /**
     * 获取对应的host
     *
     * @param hostType host类型
     * @return host
     */
    fun getHost(hostType: Int): String {
        val host: String
        when (hostType) {
            HostType.BASE_URL -> host = BASE_URL
            HostType.BASE_DUOKAN_URL -> host = BASE_DUOKAN_URL
            HostType.BASE_DUOKAN_INFO_URL -> host = BASE_DUOKAN_INFO_URL
            HostType.JUHE_URL -> host = JUHE_URL
            HostType.BAIDU_URL -> host = BAIDU_URL
            HostType.M_BAIDU_URL -> host = M_BAIDU_URL
            HostType.M_MAHUA_URL -> host = M_MAHUA_URL
            else -> host = BASE_URL
        }
        return host
    }
}
