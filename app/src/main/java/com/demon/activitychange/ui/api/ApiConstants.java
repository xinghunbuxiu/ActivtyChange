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
package com.demon.activitychange.ui.api;

public class ApiConstants {
    public static final String BASE_DUOKAN_URL = "http://www.duokan.com";
    public static final String BASE_DUOKAN_INFO_URL = "https://cdn.cnbj1.fds.api.mi-img.com/";


    public static final String BAIDU_URL = "http://app.video.baidu.com";

    public static final String M_BAIDU_URL = "https://m.baidu.com";

    public static final String BASE_URL = "http://www.wdfgm.com";

    public static final String JUHE_URL = "http://v.juhe.cn";
    private static final String M_MAHUA_URL = "http://api.hbzjmf.com";

    /**
     * 获取对应的host
     *
     * @param hostType host类型
     * @return host
     */
    public static String getHost(int hostType) {
        String host;
        switch (hostType) {
            case HostType.BASE_URL:
                host = BASE_URL;
                break;
            case HostType.BASE_DUOKAN_URL:
                host = BASE_DUOKAN_URL;
                break;
            case HostType.BASE_DUOKAN_INFO_URL:
                host = BASE_DUOKAN_INFO_URL;
                break;
            case HostType.JUHE_URL:
                host = JUHE_URL;
                break;
            case HostType.BAIDU_URL:
                host = BAIDU_URL;
                break;
            case HostType.M_BAIDU_URL:
                host = M_BAIDU_URL;
                break;
            case HostType.M_MAHUA_URL:
                host = M_MAHUA_URL;
                break;
            default:
                host = BASE_URL;
                break;
        }
        return host;
    }
}
