package com.lixh.base

import android.os.Bundle
import com.lixh.app.AppManager
import com.lixh.bean.Message
import com.lixh.presenter.BasePresenter
import com.lixh.rxhttp.Observer
import com.lixh.view.ExtandView
import com.lixh.view.IBase
import com.lixh.view.ILayout


/**
 * 基类Activity
 */
abstract class BaseMvpActivity<T : BasePresenter<*>>(isDoubleExit: Boolean = false, private var presenter: T? = null, ui: ExtandView.() -> Unit) : BaseActivity(isDoubleExit, ui), Observer<Message>, ILayout, IBase {


    override fun onCreate(savedInstanceState: Bundle?) {
        // 有 p时 初始化
        presenter?.let {
            it.attach(this)
        }
        super.onCreate(savedInstanceState)
    }


    override fun onDestroy() {
        presenter?.onDestroy()
        super.onDestroy()
        AppManager.finishActivity(this)
    }

}
