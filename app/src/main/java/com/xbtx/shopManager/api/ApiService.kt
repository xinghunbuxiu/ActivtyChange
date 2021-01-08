package com.xbtx.shopManager.api

import com.demon.activitychange.bean.OrderBean
import com.lixh.base.BaseResPose
import retrofit2.http.GET


/**
 * Created by helin on 2016/10/9 17:09.
 */

interface ApiService {

    @GET("/commApi.gsp?method=latelySuccPayOrder&deviceId=1")
    suspend fun getList(): BaseResPose<OrderBean>

}
