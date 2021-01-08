package com.lixh.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Window
import androidx.fragment.app.FragmentActivity
import com.lixh.presenter.BasePresenter
import com.lixh.setting.AppConfig
import com.lixh.statusBar.StatusBarCompat.translucentStatusBar
import com.lixh.utils.Exit
import com.lixh.utils.UStore
import com.lixh.utils.toast
import com.lixh.view.ExtandView
import com.lixh.view.ILayout
import com.lixh.view.ui


/**
 * 基类Activity
 */
abstract class BaseActivity(private val isDoubleExit: Boolean = false, ui: ExtandView.() -> Unit, private var presenter: BasePresenter<*>? = null) : FragmentActivity(), ILayout {
    //当前类需要的操作类
    private var layout: ExtandView.() -> Unit = ui
    private var exit = Exit()// 双击退出 封装
    private var mNowMode by UStore(AppConfig.ISNIGHT, false)
    lateinit var view: ExtandView

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)//无标题
        requestWindowFeature(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)// 设置竖屏
        super.onCreate(savedInstanceState)

        //初始化 View
        view = ui(layout).createView()
        // 有 p时 初始化
        presenter?.attach(this)
        //设置透明
        translucentStatusBar(this)
        init(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        view.onPostCreate()
    }

    override fun onResume() {
        super.onResume()
//        if (mNowMode) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        }
//        recreate()
    }


    override fun onDestroy() {
        presenter?.onDestroy()
        super.onDestroy()
    }


    override fun onBackPressed() {
        if (isDoubleExit) {
            if (exit.isExit) {
                view.eventBus.appExit(true)
            } else {
                exit.doExitInOneSecond()
                "再按一次退出".toast()
            }
        } else {
            super.onBackPressed()
        }
    }

}
