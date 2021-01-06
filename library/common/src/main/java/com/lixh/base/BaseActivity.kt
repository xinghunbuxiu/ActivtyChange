package com.lixh.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.lixh.app.AppManager
import com.lixh.bean.Message
import com.lixh.presenter.BasePresenter
import com.lixh.rxhttp.Observable
import com.lixh.rxhttp.Observer
import com.lixh.setting.AppConfig
import com.lixh.statusBar.StatusBarCompat.translucentStatusBar
import com.lixh.utils.*
import com.lixh.view.ExtandView
import com.lixh.view.IBase
import com.lixh.view.ILayout
import com.lixh.view.ui


/**
 * 基类Activity
 */
abstract class BaseActivity(private val isDoubleExit: Boolean = false, ui: ExtandView.() -> Unit, private var presenter: BasePresenter<*>? = null) : AppCompatActivity(), Observer<Message>, ILayout, IBase {
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
        presenter?.let {
            it.attach(this)
        }
        // 把actvity放到application栈中管理
        AppManager.addActivity(this)
        Global.get()?.addObserver(this)
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
        if (mNowMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        recreate()
    }


    override fun onDestroy() {
        presenter?.onDestroy()
        super.onDestroy()
        AppManager.finishActivity(this)
    }


    override fun onBackPressed() {
        if (isDoubleExit) {
            if (exit.isExit) {
                AppManager.notifyExitApp()
            } else {
                exit.doExitInOneSecond()
                showShort("再按一次退出")
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun update(o: Observable, arg: Message) {
        ULog.e(arg.toString())

    }
}
