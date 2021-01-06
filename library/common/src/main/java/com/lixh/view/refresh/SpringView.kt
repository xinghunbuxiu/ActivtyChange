package com.lixh.view.refresh

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ListView
import android.widget.OverScroller
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import com.lixh.view.refresh.ImplPull.ScrollState
import com.lixh.view.refresh.ImplPull.StateType
import kotlin.math.abs

/**
 * Created by LIXH on 2017/1/3.
 * email lixhVip9@163.com
 * des
 */

open class SpringView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {
    private var mFooter: FooterView? = null
    private var mHeader: HeaderView? = null
    private var mChildView: View? = null
    //最大拉动距离，拉动距离越靠近这个值拉动就越缓慢
    private var MAX_HEADER_PULL_HEIGHT = 600
    private val MAX_FOOTER_PULL_HEIGHT = 600
    private var onRefreshListener: OnRefreshListener? = null
    private var onLoadListener: OnLoadListener? = null
    private var implPull: ImplPull? = null
    private var mLastY: Float = 0f
    private var isChangeFocus = false
    private val MOVE_PARA = 2.0
    private var isNeedMyMove: Boolean = false
    private var needResetAnim: Boolean = false
    private var scrollState = ScrollState.NONE
    private var stateType = StateType.NONE
    private val MOVE_TIME = 400

    private var isFirstLoad = true

    private var mScroller: OverScroller = OverScroller(context)

    private val isTop: Boolean
        get() = !CanPullUtil.canChildScrollUp(mChildView)

    private val isBottom: Boolean
        get() = !CanPullUtil.canChildScrollDown(mChildView)


    private var dy: Float = 0f
    private var dx: Float = 0f
    private var mLastX: Float = 0f


    /**
     * 处理多点触控的情况，准确地计算Y坐标和移动距离dy
     * 同时兼容单点触控的情况
     */
    private var mActivePointerId = MotionEvent.INVALID_POINTER_ID

    private var autoRefresh = true


    interface OnRefreshListener {
        fun onRefresh()
    }

    interface OnLoadListener {
        fun onLoad()
    }

    fun setOnLoadListener(onRefreshListener: OnLoadListener) {
        this.onLoadListener = onRefreshListener
    }

    fun setStateType(stateType: StateType) {
        this.stateType = stateType
        implPull?.onScrollChange(stateType)
    }

    fun setOnRefreshListener(onRefreshListener: OnRefreshListener) {
        this.onRefreshListener = onRefreshListener
    }

    init {
        if (childCount > 1) {
            throw RuntimeException("can only have one child widget")
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        if (mHeader != null && onRefreshListener != null) {
            val th = mHeader!!.dragMaxHeight
            MAX_HEADER_PULL_HEIGHT = if (th > 0) th else MAX_HEADER_PULL_HEIGHT
        }
        if (mFooter != null && onLoadListener != null) {
            val bh = mFooter!!.dragMaxHeight
            MAX_HEADER_PULL_HEIGHT = if (bh > 0) bh else MAX_HEADER_PULL_HEIGHT
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val context = context
        onRefreshListener?.let {
            mHeader ?: CustomHeadView(context, this)
            addView(mHeader?.view, 0)
        }

        if (mChildView == null) {
            mChildView = getChildAt(childCount - 1)
        }
        onLoadListener?.let {
            mFooter ?: CustomFootView(context, this)
            addView(mFooter?.view, childCount)
        }
        this.implPull = mHeader
        if (autoRefresh && isFirstLoad) {
            needResetAnim = true
            isFirstLoad = false
            updating()
        }
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        dealMulTouchEvent(ev)
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                scrollState = ScrollState.NONE
                isNeedMyMove = false
            }
            MotionEvent.ACTION_MOVE -> {
                isNeedMyMove = isNeedMyMove()
                if (isNeedMyMove && !isChangeFocus) {
                    isChangeFocus = true
                    return resetDispatchTouchEvent(ev)
                }
            }
            MotionEvent.ACTION_UP -> needResetAnim = true
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun resetDispatchTouchEvent(ev: MotionEvent): Boolean {
        ev.action = MotionEvent.ACTION_CANCEL
        val newEvent = MotionEvent.obtain(ev)
        dispatchTouchEvent(ev)
        newEvent.action = MotionEvent.ACTION_DOWN
        return dispatchTouchEvent(newEvent)
    }

    /**
     * 判断是否需要由该控件来控制滑动事件
     */
    private fun isNeedMyMove(): Boolean {
        if (abs(dy) < abs(dx)) {
            return false
        }
        if (dy > 0 && isTop || scrollY < 0 - 20) {
            scrollState = ScrollState.TOP
            implPull = mHeader
            return true
        }
        if (dy < 0 && isBottom || scrollY > 0 + 20) {
            scrollState = ScrollState.BOTTOM
            implPull = mFooter
            return true
        }
        return false
    }

    private fun dealMulTouchEvent(ev: MotionEvent) {
        when (MotionEventCompat.getActionMasked(ev)) {
            MotionEvent.ACTION_DOWN -> {
                val pointerIndex = MotionEventCompat.getActionIndex(ev)
                val x = MotionEventCompat.getX(ev, pointerIndex)
                val y = MotionEventCompat.getY(ev, pointerIndex)
                mLastX = x
                mLastY = y
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0)
            }
            MotionEvent.ACTION_MOVE -> {
                val pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId)
                val x = MotionEventCompat.getX(ev, pointerIndex)
                val y = MotionEventCompat.getY(ev, pointerIndex)
                dx = x - mLastX
                dy = y - mLastY
                mLastY = y
                mLastX = x
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> mActivePointerId = MotionEvent.INVALID_POINTER_ID
            MotionEvent.ACTION_POINTER_DOWN -> {
                val pointerIndex = MotionEventCompat.getActionIndex(ev)
                val pointerId = MotionEventCompat.getPointerId(ev, pointerIndex)
                if (pointerId != mActivePointerId) {
                    mLastX = MotionEventCompat.getX(ev, pointerIndex)
                    mLastY = MotionEventCompat.getY(ev, pointerIndex)
                    mActivePointerId = MotionEventCompat.getPointerId(ev, pointerIndex)
                }
            }
            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndex = MotionEventCompat.getActionIndex(ev)
                val pointerId = MotionEventCompat.getPointerId(ev, pointerIndex)
                if (pointerId == mActivePointerId) {
                    val newPointerIndex = if (pointerIndex == 0) 1 else 0
                    mLastX = MotionEventCompat.getX(ev, newPointerIndex)
                    mLastY = MotionEventCompat.getY(ev, newPointerIndex)
                    mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex)
                }
            }
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return isNeedMyMove
    }


    /**
     * 第一次加载时自动下拉刷新
     *
     * @param autoRefresh
     */
    fun setAutoRefresh(autoRefresh: Boolean) {
        this.autoRefresh = autoRefresh
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {

        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
            }
            MotionEvent.ACTION_MOVE -> if (isNeedMyMove) {
                needResetAnim = false      //按下的时候关闭回弹
                doMove()
            } else {
                if (dy != 0f && scrollY > -30 && scrollY < 30) {
                    scrollBy(0, -scrollY)
                    isChangeFocus = false
                    e.action = MotionEvent.ACTION_DOWN
                    dispatchTouchEvent(e)
                }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> eventUp()
        }

        return true

    }

    private fun doMove() {

        //根据下拉高度计算位移距离，（越拉越慢）
        var moveX: Int = when {
            dy > 0 -> ((MAX_HEADER_PULL_HEIGHT + scrollY) / MAX_HEADER_PULL_HEIGHT.toFloat() * dy / MOVE_PARA).toInt()
            else -> ((MAX_FOOTER_PULL_HEIGHT - scrollY) / MAX_FOOTER_PULL_HEIGHT.toFloat() * dy / MOVE_PARA).toInt()
        }
        scrollBy(0, -moveX)
    }

    private fun eventUp() {
        if (stateType == StateType.RELEASE) {
            if (scrollState == ScrollState.TOP) {
                updating()
            } else if (scrollState == ScrollState.BOTTOM) {
                upLoading()
            }
        } else {
            dy = 0f
            if (scrollState == ScrollState.BOTTOM) {

                if (mChildView is AbsListView) {
                    (mChildView as ListView).smoothScrollBy(scrollY, 0)
                } else if (mChildView is RecyclerView) {
                    (mChildView as RecyclerView).scrollBy(0, scrollY)
                }
            }
            mScroller!!.startScroll(0, scrollY, 0, -scrollY, MOVE_TIME)
            invalidate()

        }
    }

    override fun computeScroll() {
        if (mScroller!!.computeScrollOffset()) {
            super.scrollTo(0, mScroller!!.currY)
            invalidate()
        }
    }

    override fun scrollTo(x: Int, y: Int) {
        implPull?.let { implPull ->
            implPull.scroll(MAX_HEADER_PULL_HEIGHT, y)
            if (scrollState == ScrollState.TOP) {
                if (y > -implPull.height) {
                    setStateType(StateType.PULL)
                } else {
                    setStateType(StateType.RELEASE)
                }
            } else if (scrollState == ScrollState.BOTTOM) {
                if (y < implPull.height) {
                    setStateType(StateType.PULL)
                } else {
                    setStateType(StateType.RELEASE)
                }
            }
            if (y != scrollY) {
                super.scrollTo(x, y)
            }
        }

    }


    private fun updating() {
        onRefreshListener?.let {
            setStateType(StateType.LOADING)
            scrollState = ScrollState.NONE
            mScroller.startScroll(0, scrollY, 0, -scrollY - implPull!!.height, MOVE_TIME)
            invalidate()
            postDelayed({ it.onRefresh() }, 400)
        }

    }

    private fun upLoading() {
        onLoadListener?.let {
            setStateType(StateType.LOADING)
            scrollState = ScrollState.NONE
            mScroller.startScroll(0, scrollY, 0, -scrollY + implPull!!.height, MOVE_TIME)
            invalidate()
            postDelayed({ it.onLoad() }, 400)
        }
    }

    fun finishRefreshAndLoadMore() {
        setStateType(StateType.LOAD_CLOSE)
        this.postDelayed({
            if (needResetAnim) {
                eventUp()
                setStateType(StateType.NONE)
            }
        }, 400)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        onRefreshListener?.let {
            mHeader?.view?.layout(0, 0 - mHeader!!.height, r, 0)
        }
        mChildView?.layout(0, 0, r, mChildView!!.measuredHeight)

        onLoadListener?.let {
            mFooter?.view?.layout(0, mChildView!!.measuredHeight, r, mFooter!!.height + mChildView!!.measuredHeight)
        }
    }

}
