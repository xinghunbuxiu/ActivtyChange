package com.lixh.view.refresh

import android.content.Context
import com.lixh.R
import kotlinx.android.synthetic.main.view_refresh_header_normal.view.*
import kotlin.math.abs

/**
 * Created by LIXH on 2017/1/3.
 * email lixhVip9@163.com
 * des
 */

class CustomHeadView(context: Context, springView: SpringView) : HeaderView(context, R.layout.view_refresh_header_normal, springView) {


    override fun onScrollChange(state: ImplPull.StateType) {
        when (state) {
            ImplPull.StateType.NONE, ImplPull.StateType.PULL -> view.tv_normal_refresh_header_status.text = "下拉刷新..."
            ImplPull.StateType.RELEASE -> view.tv_normal_refresh_header_status.text = "释放刷新..."
            ImplPull.StateType.LOADING -> view.tv_normal_refresh_header_status.text = "正在刷新..."
            ImplPull.StateType.LOAD_CLOSE -> view.tv_normal_refresh_header_status.text = "刷新结束..."
        }
    }


    override fun scroll(maxY: Int, y: Int) {
        var currentProgress = abs(y / height)
        if (currentProgress >= 11) {
            currentProgress = 11
        }
        view.iv_normal_refresh_header.drawable.level = currentProgress
    }


}
