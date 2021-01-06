package com.lixh.view.refresh

import android.content.Context
import com.lixh.R
import com.nineoldandroids.animation.ObjectAnimator
import com.nineoldandroids.view.ViewHelper
import kotlinx.android.synthetic.main.view_normal_refresh_footer.view.*

/**
 * Created by LIXH on 2017/1/3.
 * email lixhVip9@163.com
 * des
 */

class CustomFootView(context: Context, springView: SpringView) : FooterView(context, R.layout.view_normal_refresh_footer, springView) {


    override fun scroll(maxY: Int, y: Int) {

    }

    override fun onScrollChange(state: ImplPull.StateType) {
        when (state) {
            ImplPull.StateType.NONE -> {
                ObjectAnimator.clearAllAnimations()
                view.tv_normal_refresh_footer_status.text = "上拉加载..."
            }
            ImplPull.StateType.PULL -> view.tv_normal_refresh_footer_status.text = "上拉加载..."
            ImplPull.StateType.RELEASE -> view.tv_normal_refresh_footer_status.text = "释放加载..."
            ImplPull.StateType.LOADING -> {
                view.tv_normal_refresh_footer_status.text = "正在加载..."
                AnimUtil.startRotation(view.iv_normal_refresh_footer_chrysanthemum, ViewHelper.getRotation(view.iv_normal_refresh_footer_chrysanthemum) + 359.99f, 500, 0, -1)
            }
            ImplPull.StateType.LOAD_CLOSE -> view.tv_normal_refresh_footer_status.text = "加载完毕..."
        }
    }
}
