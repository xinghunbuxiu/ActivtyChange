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
package com.xbtx.shopManager.api

import com.demon.activitychange.ui.api.HostType

object ApiConstants {

    val BASE_URL = "http://mall.xunbao88.com.cn"


    /**
     * 获取对应的host
     *
     * @param hostType host类型
     * @return host
     */
    fun getHost(hostType: Int): String {
        return when (hostType) {
            HostType.BASE_URL -> BASE_URL
            else -> BASE_URL
        }
    }
}
