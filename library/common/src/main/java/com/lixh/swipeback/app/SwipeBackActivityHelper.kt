package com.lixh.swipeback.app

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

import com.lixh.swipeback.Utils

/**
 * @author Yrom
 */
class SwipeBackActivityHelper(private val mActivity: Activity) {

    var swipeBackLayout: SwipeBackLayout? = null
        private set

    fun onActivityCreate() {
        mActivity.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mActivity.window.decorView.setBackgroundDrawable(null)
        swipeBackLayout = SwipeBackLayout(mActivity)
        swipeBackLayout!!.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        swipeBackLayout!!.addSwipeListener(object : SwipeBackLayout.SwipeListener {
            override fun onScrollStateChange(state: Int, scrollPercent: Float) {}

            override fun onEdgeTouch(edgeFlag: Int) {
                Utils.convertActivityToTranslucent(mActivity)
            }

            override fun onScrollOverThreshold() {

            }
        })
    }
    
    fun findViewById(id: Int): View? {
        return swipeBackLayout?.findViewById(id)
    }
}
