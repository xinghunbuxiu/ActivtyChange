package com.lixh.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.IntDef
import com.lixh.R
import kotlinx.android.synthetic.main.dialog_loading_tip.view.*
import kotlinx.android.synthetic.main.notification_view.view.*
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy


/**
 * des:加载页面内嵌提示
 * Created by xsf
 * on 2016.07.17:22
 */
class LoadingTip @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val errorMsg: String? = null
    val loadingMsg: String? = null
    val emptyMsg: String? = null
    val noContentTip: Int? = null
    var onReloadListener: OnReloadListener? = null

    interface OnReloadListener {
        fun reload()
    }


    /**
     * 根据状态显示不同的提示
     * @param loadStatus
     */
    fun setLoadingTip(@LoadStatus loadStatus: Int) {
        when (loadStatus) {
            LoadStatus.EMPTY -> {
                visibility = View.VISIBLE
                img_tip_logo?.visibility = View.VISIBLE
                progress?.visibility = View.GONE
                tv_tips?.text = emptyMsg ?: context.getText(R.string.empty).toString()
                img_tip_logo?.setImageResource(noContentTip ?: R.mipmap.no_content_tip)
            }
            LoadStatus.SERVER_ERROR, LoadStatus.NET_ERROR -> {
                visibility = View.VISIBLE
                img_tip_logo?.visibility = View.VISIBLE
                progress?.visibility = View.GONE
                tv_tips?.text = errorMsg ?: context.getText(R.string.net_error).toString()
                img_tip_logo?.setImageResource(if (loadStatus == LoadStatus.SERVER_ERROR) R.mipmap.ic_wrong else R.mipmap.ic_wifi_off)
            }
            LoadStatus.LOADING -> {
                visibility = View.VISIBLE
                img_tip_logo?.visibility = View.GONE
                progress?.visibility = View.VISIBLE
                tv_tips?.text = loadingMsg ?: context.getText(R.string.loading).toString()
            }
            LoadStatus.FINISH -> visibility = View.GONE
        }
    }

    init {
        View.inflate(context, R.layout.dialog_loading_tip, this)
        //重新尝试
        bt_operate?.setOnClickListener {
            onReloadListener?.reload()
        }
        visibility = View.GONE
    }


    @IntDef(LoadStatus.SHOW_LOAD_MORE_ERROR, //分页加载失败
            LoadStatus.SERVER_ERROR, //分为服务器失败
            LoadStatus.NET_ERROR, //网络加载失败
            LoadStatus.EMPTY, //数据为空
            LoadStatus.LOADING, //加载中
            LoadStatus.FINISH)//完成
    @Retention(RetentionPolicy.SOURCE)
    annotation class LoadStatus {
        companion object {
            const val SHOW_LOAD_MORE_ERROR = 1
            const val SERVER_ERROR = 2
            const val NET_ERROR = 3
            const val EMPTY = 4
            const val LOADING = 5
            const val FINISH = 6
        }
    }


}

