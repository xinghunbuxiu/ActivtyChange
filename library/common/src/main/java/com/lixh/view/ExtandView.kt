package com.lixh.view

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.lixh.R
import com.lixh.swipeback.Utils
import com.lixh.swipeback.app.SwipeBackActivityHelper
import com.lixh.swipeback.app.SwipeBackLayout
import com.lixh.utils.UView
import kotlinx.android.synthetic.main.common_tab_layout.view.*


/**
 * Created by LIXH on 2016/11/5.
 * email lixhVip9@163.com
 * des
 */

open class ExtandView(view: IBase) {
    var base: IBase = view
    var supportFragmentManager: FragmentManager?
    var activity: FragmentActivity? = null
    lateinit var root: RelativeLayout
    private var contentTop: Boolean = true
    var mSlideMenu: SlideMenu? = null
    var toolbar: UToolBar? = null
    val swipeBackLayout: SwipeBackLayout? = null

    init {
        activity = when (view) {
            is FragmentActivity -> view
            is Fragment -> view.activity
            else -> null
        }
        activity?.let {
            this.root = MyRelativeLayout(it).run {
                isCj = contentTop
                this
            }
        }
        this.supportFragmentManager = activity?.supportFragmentManager

    }

    val lParams: RelativeLayout.LayoutParams
        get() = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

    //标题栏 默认 toolBar
    inline fun titleBar(block: UToolBar.() -> Unit) = toolbar ?: titleBar(R.id.toolbar, block)

    //标题栏 自定义
    inline fun <T : View> titleBar(resId: Int, block: T.() -> Unit) =
            UView.inflate<T>(activity!!, resId).run {
                block(this)
            }


    //正文 设置 layout 
    inline fun <T : View> body(resId: Int, isBelowTitle: Boolean = true, block: T.() -> T) = UView.inflate<T>(activity!!, resId).let {
        root.addView(it, lParams.apply {
            when {
                isBelowTitle && toolbar != null -> addRule(RelativeLayout.BELOW, it.id)
            }
        })
        it
    }.run {
        block(this)
    }

    fun body(resId: Int, isBelowTitle: Boolean = true) = UView.inflate<View>(activity!!, resId).let {
        root.addView(it, lParams.apply {
            when {
                isBelowTitle && toolbar != null -> addRule(RelativeLayout.BELOW, it.id)
            }
        })
    }

    //正文
    fun body(view: View, isBelowTitle: Boolean = true) = view.let {
        root.addView(it, lParams.apply {
            when {
                isBelowTitle && toolbar != null -> addRule(RelativeLayout.BELOW, it.id)
            }
        })
        it
    }


    fun onPostCreate() {
        swipeBackLayout?.attachToActivity(activity!!)
    }

    fun scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(activity!!)
        swipeBackLayout?.scrollToFinishActivity()
    }

    //侧滑返回
    inline fun swipeBack(block: SwipeBackLayout?.() -> Unit) = swipeBackLayout
            ?: SwipeBackActivityHelper(activity!!).let {
                it.onActivityCreate()
                it.swipeBackLayout?.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT)
                it.swipeBackLayout?.setEnableGesture(true)
                it.swipeBackLayout
            }.run {
                block(this)
            }

    //侧滑
    inline fun slideMenu(leftView: BaseSlideView, block: SlideMenu.() -> Unit) = mSlideMenu
            ?: with(SlideMenu(activity!!)) {
                id = R.id.slideMenu
                contentView = root
                slideView = leftView.view
                layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
                this
            }.run {
                block(this)
            }

    //底部tab  默认是
    inline fun bottomBar(fragments: Array<Fragment>, items: Array<out BottomNavigationItem>, listener: OnTabSelectedListener? = null, index: Int = 0, block: BottomNavigationBar.() -> Unit): BottomNavigationBar = body<FrameLayout>(R.layout.common_tab_layout) {
        for (item in items) {
            bottom_navigation_bar.addItem(item)
        }
        bottom_navigation_bar.setMode(BottomNavigationBar.MODE_FIXED)
        bottom_navigation_bar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
    }.let {
        /**
         * 设置默认的
         */
        supportFragmentManager?.let { transition ->
            it.bottom_navigation_bar.setTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener {
                override fun onTabSelected(position: Int) {
                    if (listener?.onTabSelected(position) == true) {
                        return
                    }
                    if (position < fragments.size) {
                        val ft = transition.beginTransaction()
                        val fragment = fragments[position]
                        if (fragment.isAdded) {
                            ft.replace(it.layFrame.id, fragment)
                        } else {
                            ft.add(it.layFrame.id, fragment)
                        }
                        ft.commitAllowingStateLoss()
                    }

                }

                override fun onTabUnselected(position: Int) {
                    if (listener?.onTabUnselected(position) == true) {
                        return
                    }
                    if (position < fragments.size) {
                        val ft = transition.beginTransaction()
                        val fragment = fragments[position]
                        ft.remove(fragment)
                        ft.commitAllowingStateLoss()
                    }
                }

                override fun onTabReselected(position: Int) {
                    if (listener?.onTabReselected(position) == true) {
                        return
                    }
                }

            })
            //设置默认为 第一个 Item
            val transaction = transition.beginTransaction()
            transaction.replace(it.layFrame.id, fragments[index])
            transaction.commit()
        }
        it.bottom_navigation_bar
    }.run {
        block(this)
        this
    }


    fun createView(): ExtandView {
        activity?.setContentView(root)
        return this
    }


}
