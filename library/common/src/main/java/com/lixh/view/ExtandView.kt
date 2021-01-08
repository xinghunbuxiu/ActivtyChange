package com.lixh.view

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.lixh.R
import com.lixh.rxhttp.EventBus
import com.lixh.swipeback.Utils
import com.lixh.swipeback.app.SwipeBackActivityHelper
import com.lixh.swipeback.app.SwipeBackLayout
import com.lixh.utils.UView
import com.lixh.utils.log
import kotlinx.android.synthetic.main.common_tab_layout.view.*


/**
 * Created by LIXH on 2016/11/5.
 * email lixhVip9@163.com
 * des
 */

open class ExtandView(view: LifecycleOwner) {
    var base: LifecycleOwner = view
    var supportFragmentManager: FragmentManager?
    var activity: FragmentActivity? = null
    lateinit var root: RelativeLayout
    private var contentTop: Boolean = true
    private var mSlideMenu: SlideMenu? = null
    private var toolbar: UToolBar? = null
    private var swipeBackLayout: SwipeBackLayout? = null
    var eventBus: EventBus = EventBus()

    init {
        activity = when (view) {
            is FragmentActivity -> {
                view.lifecycle.addObserver(eventBus)
                view
            }
            is Fragment -> {
                view.lifecycle.addObserver(eventBus)
                view.activity
            }
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
    fun titleBar(block: UToolBar.() -> Unit) = toolbar ?: titleBar(R.layout.toolbar) {
        toolbar = this
        block(this)
    }

    //标题栏 自定义
    fun <T : View> titleBar(resId: Int, block: T.() -> Unit) =
            UView.inflate<T>(activity!!, resId, root).run {
                block(this)
                this
            }.apply {
                root.addView(this)
            }

    fun <T : View> addBody(view: T, isBelowTitle: Boolean = true, block: T .() -> T): T {
        "ssss".log()
        root.addView(view, lParams.apply {
            "ssss1$isBelowTitle$toolbar".log()
            if (isBelowTitle && toolbar != null) {
                toolbar!!.id.log()
                addRule(RelativeLayout.BELOW, toolbar!!.id)
            }
        })
        return view.run {
            block(this)
        }
    }

    //正文 设置 layout 
    fun <T : View> body(resId: Int, isBelowTitle: Boolean = true, block: T .() -> T) = addBody(UView.inflate<T>(activity!!, resId), isBelowTitle, block)

    fun body(resId: Int, isBelowTitle: Boolean = true) = addBody(UView.inflate<ViewGroup>(activity!!, resId), isBelowTitle) {
        this
    }

    //正文
    fun body(view: View, isBelowTitle: Boolean = true) = addBody(view, isBelowTitle) { this }


    fun onPostCreate() {
        swipeBackLayout?.attachToActivity(activity!!)
    }

    fun scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(activity!!)
        swipeBackLayout?.scrollToFinishActivity()
    }
    //侧滑返回
    fun swipeBack(block: SwipeBackLayout?.() -> Unit) = swipeBackLayout
            ?: SwipeBackActivityHelper(activity!!).let {
                swipeBackLayout = it.swipeBackLayout
                it.onActivityCreate()
                it.swipeBackLayout?.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT)
                it.swipeBackLayout?.setEnableGesture(true)
                swipeBackLayout
            }.run {
                block(this)
            }

    //侧滑
    fun slideMenu(leftView: BaseSlideView, block: SlideMenu.() -> Unit) = mSlideMenu
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
    fun bottomBar(fragments: Array<Fragment>, items: Array<out BottomNavigationItem>, listener: OnTabSelectedListener? = null, index: Int = 0, block: BottomNavigationBar.() -> Unit): BottomNavigationBar = body<LinearLayout>(R.layout.common_tab_layout) {
        "走了这里$items".log()
        for (item in items) {
            bottom_navigation_bar.addItem(item)
        }
        bottom_navigation_bar.setActiveColor("#000000")
        bottom_navigation_bar.setMode(BottomNavigationBar.MODE_FIXED)
        bottom_navigation_bar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
        /**
         * 设置默认的
         */
        supportFragmentManager?.let { transition ->
            bottom_navigation_bar.setTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener {
                override fun onTabSelected(position: Int) {
                    if (listener?.onTabSelected(position) == true) {
                        return
                    }
                    if (position < fragments.size) {
                        val ft = transition.beginTransaction()
                        val fragment = fragments[position]
                        if (fragment.isAdded) {
                            ft.replace(layFrame.id, fragment)
                        } else {
                            ft.add(layFrame.id, fragment)
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
            transaction.replace(layFrame.id, fragments[index])
            transaction.commit()
        }
        bottom_navigation_bar.setFirstSelectedPosition(index)
                .initialise(); //initialise 一定要放在 所有设置的最后一项
        this
    }.bottom_navigation_bar.apply(block)


    fun createView(): ExtandView {
        activity?.setContentView(root)
        return this
    }


}
