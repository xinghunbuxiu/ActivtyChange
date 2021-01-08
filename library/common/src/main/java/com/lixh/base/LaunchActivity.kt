package com.lixh.base

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import com.lixh.utils.PermissionUtils
import com.lixh.utils.extend.utils.startActivity
import com.sembozdemir.permissionskt.askPermissions

/**
 * @author lixh
 * @version V1.0
 * @Title: WelcomeActivity.java
 * @Package com.cihon.activities
 * @Description: 欢迎界面
 * @date 2015年5月12日 上午10:25:24
 */
abstract class LaunchActivity(@LayoutRes val layoutId: Int, private val isFirst: Boolean = false) : FragmentActivity() {

    /**
     * Handler:跳转到不同界面
     */
    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            startActivity(toActivity(msg.what))
            finish()
            super.handleMessage(msg)
        }
    }

    abstract fun toActivity(what: Int): Class<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)//无标题
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        initView()
    }

    private fun initRxPermissions() {
        askPermissions(*PermissionUtils.PERMISSIONS) {
            onGranted {
                mHandler.sendEmptyMessageDelayed(if (isFirst) GO_GUIDE else GO_HOME,
                        SPLASH_DELAY_MILLIS)
            }
            onDenied { permissions ->
                permissions.forEach {
                    Log.d(TAG, "Call $it is denied")
                }
                mHandler.sendEmptyMessageDelayed(if (isFirst) GO_GUIDE else GO_HOME,
                        SPLASH_DELAY_MILLIS)
            }
        }
    }

    open fun initView() {
        if (!this.isTaskRoot) { // 判断该Activity是不是任务空间的源Activity，“非”也就是说是被系统重新实例化出来
            // 如果你就放在launcher Activity中话，这里可以直接return了
            val mainIntent = getIntent()
            val action = mainIntent.action
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action == Intent.ACTION_MAIN) {
                finish()
                return // finish()之后该活动会继续执行后面的代码，你可以logCat验证，加return避免可能的exception
            }
        }
        initRxPermissions()
    }

    companion object {
        const val TAG = "LaunchActivity"
        const val GO_HOME = 1
        const val GO_GUIDE = 2
        const val SPLASH_DELAY_MILLIS: Long = 1000
    }


}
