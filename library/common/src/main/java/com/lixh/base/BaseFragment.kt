package com.lixh.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lixh.presenter.BasePresenter
import com.lixh.view.ExtandView
import com.lixh.view.ILayout
import com.lixh.view.ui

abstract class BaseFragment(ui: ExtandView.() -> Unit, private var presenter: BasePresenter<*>? = null) : Fragment(), ILayout {
    private var mContentView: View? = null
    private var layout: ExtandView.() -> Unit = ui
    lateinit var view: ExtandView
    override fun onAttach(context: Context) {
        super.onAttach(context)
        view = ui(layout)
        // 有 p时 初始化
        presenter?.attach(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mContentView == null) {
            mContentView = view.root
            init(savedInstanceState)
        }
        return mContentView
    }


    override fun onDetach() {
        super.onDetach()
        presenter?.onDestroy()
    }
}
