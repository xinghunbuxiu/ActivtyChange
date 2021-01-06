package com.lixh.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.SystemClock
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.widget.FrameLayout
import android.widget.OverScroller
import androidx.annotation.Nullable
import androidx.core.view.MotionEventCompat
import com.lixh.swipeback.app.SwipeBackLayout.Companion.EDGE_LEFT
import com.lixh.swipeback.app.SwipeBackLayout.Companion.EDGE_RIGHT
import com.nineoldandroids.view.ViewHelper
import java.util.*
import kotlin.math.abs

/**
 *
 * @des 侧滑
 */
class SlideMenu @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                          defStyleAttr: Int = 0

) : FrameLayout(context, attrs, defStyleAttr) {
    @Nullable
    lateinit var slideView: View
    @Nullable
    lateinit var contentView: View

    @State
    internal var slideState = State.CLOSE
    //参数
    private var scroller: OverScroller = OverScroller(context)
    private val density = context.resources.displayMetrics.density
    var edgeEnable: Boolean = true//是否需要边缘触控
    var isFollowing: Boolean = false// 跟随移动时
    var collapseOffset: Int = 200
    var scale: Float = 0.toFloat()//滑动时的收缩比例
    var line: Int = 5//将横屏分为5份
    var EDGE_SIZE: Int = 20 // dp
    var DEFAULT_COLOR: String = "#1f1f1f" //default foreground color
    val SNAP_VELOCITY: Int = 600
    var slideListener: OnSlideListener? = null


    private val mEdgeSize: Int = (EDGE_SIZE * density + 0.5f).toInt()
    /**
     * 处理多点触控的情况，准确地计算Y坐标和移动距离dy
     * 同时兼容单点触控的情况
     */
    private var mActivePointerId = MotionEvent.INVALID_POINTER_ID
    private var mVelocityTracker: VelocityTracker? = null

    var slideWidth: Int = 0
    @Slide
    var slide: Int = Slide.NONE
    private var isAnim: Boolean = false
    private var mPointersDown: Int = 0

    private var canDrag: Boolean = false//边缘触控未唤醒时是否允许拖拽
    var mInitialEdgesTouched: IntArray? = null
    private var dx: Float = 0.toFloat()
    private var dy: Float = 0.toFloat()

    private var mLastX: Float = 0.toFloat()
    private var mLastY: Float = 0.toFloat()

    private var mTrackingEdges = slide

    private var isMove: Boolean = false

    private val mPeekRunnable = Runnable { peekDrawer() }

    private var mChildrenCanceledTouch = false

    init {
        foreground = ColorDrawable(Color.parseColor(DEFAULT_COLOR))
        foreground.alpha = 0
    }


    private fun setSlideState(slideState: Int) {
        this.slideState = slideState
        slideListener?.slideState(slideState)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        collapseOffset = widthSize / line
        scale = 1f / (line - 1)
        slideView.let {
            slideWidth = widthSize - collapseOffset
            it.layoutParams.width = slideWidth
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val action = MotionEventCompat.getActionMasked(ev)
        var interceptForTap = false
        initVelocityTracker()
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                interceptForTap = false
                isMove = false
            }
            MotionEvent.ACTION_MOVE -> {
                if (abs(dx) > abs(dy)) {
                    removeCallbacks(mPeekRunnable)
                    if (isFollowing || slideState == State.OPEN) {
                        isMove = true
                    }
                } else {
                    if (slideState == State.OPEN) {
                        cancelChildViewTouch()
                    }

                }
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                if (slideState == State.OPEN && (slide == Slide.RIGHT && ev.x <= collapseOffset || slide == Slide.LEFT && ev.x >= slideWidth)) {
                    cancelChildViewTouch()
                    scrollBy((-dx).toInt(), 0)
                    close()
                }
                mChildrenCanceledTouch = false
            }
        }

        return interceptForTap || canDrag || isMove
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (MotionEventCompat.getActionMasked(ev)) {
            MotionEvent.ACTION_DOWN -> {
                mChildrenCanceledTouch = false
            }
            MotionEvent.ACTION_MOVE -> {
                if (edgeEnable) {//是否允许边缘触控
                    if (canDrag || slideState == State.OPEN || isFollowing) {
                        scrollBy((-dx).toInt(), 0)
                    }
                } else {
                    scrollBy((-dx).toInt(), 0)
                }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                removeCallbacks(mPeekRunnable)
                mChildrenCanceledTouch = false
                canDrag = false
                eventUp()
                if (mVelocityTracker != null) {
                    mVelocityTracker!!.recycle()
                    mVelocityTracker = null
                }
            }
        }
        return true
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val action = MotionEventCompat.getActionMasked(ev)
        val pointerIndex = MotionEventCompat.getActionIndex(ev)
        initVelocityTracker()
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mChildrenCanceledTouch = false
                if (mVelocityTracker != null) {
                    // Add a user's movement to the tracker.
                    mVelocityTracker!!.addMovement(ev)
                }
                val pointerId = ev.getPointerId(0)
                val x = ev.getX(pointerIndex)
                val y = ev.getY(pointerIndex)
                mLastX = x
                mLastY = y
                saveInitialMotion(mLastX, mLastY, pointerId)
                if (mActivePointerId != pointerId) {
                    mActivePointerId = pointerId
                }
                val edgesTouched = mInitialEdgesTouched!![pointerId]
                if (edgesTouched and mTrackingEdges != 0 && edgeEnable) {
                    onEdgeTouched()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (mVelocityTracker != null) {
                    // Add a user's movement to the tracker.
                    mVelocityTracker!!.addMovement(ev)
                }
                val index = ev.findPointerIndex(mActivePointerId)
                val x = ev.getX(index)
                val y = ev.getY(index)
                dx = x - mLastX
                dy = y - mLastY
                mLastY = y
                mLastX = x
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> cancel()
            MotionEventCompat.ACTION_POINTER_DOWN -> {
                val pointerId = ev.getPointerId(pointerIndex)
                mLastX = ev.getX(pointerIndex)
                mLastY = ev.getY(pointerIndex)
                saveInitialMotion(mLastX, mLastY, pointerId)
                if (mActivePointerId != pointerId) {
                    mActivePointerId = pointerId
                }
                val edgesTouched = mInitialEdgesTouched!![pointerId]
                if (edgesTouched and mTrackingEdges != 0 && edgeEnable) {
                    onEdgeTouched()
                }
            }
            MotionEventCompat.ACTION_POINTER_UP -> {
                val pointerId = ev.getPointerId(pointerIndex)
                if (pointerId == mActivePointerId) {
                    val newPointerIndex = if (pointerIndex == 0) 1 else 0
                    mLastX = ev.getX(newPointerIndex)
                    mLastY = ev.getY(newPointerIndex)
                    mActivePointerId = ev.getPointerId(newPointerIndex)
                }
                clearMotionHistory(pointerId)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun initVelocityTracker() {
        if (mVelocityTracker == null) {
            // Retrieve a new VelocityTracker object to watch the velocity of a motion.
            mVelocityTracker = VelocityTracker.obtain()
        }
    }

    private fun clearMotionHistory(pointerId: Int) {
        if (mInitialEdgesTouched == null || !isPointerDown(pointerId)) {
            return
        }
        mInitialEdgesTouched!![pointerId] = 0
        mPointersDown = mPointersDown and (1 shl pointerId).inv()
    }

    private fun isPointerDown(pointerId: Int): Boolean {
        return mPointersDown and (1 shl pointerId) != 0
    }

    private fun cancel() {
        mActivePointerId = MotionEvent.INVALID_POINTER_ID
        if (mInitialEdgesTouched != null) {
            Arrays.fill(mInitialEdgesTouched, 0)
            mLastY = 0f
            mLastX = 0f
            dx = 0f
            dy = 0f
            mPointersDown = 0
        }
    }

    private fun onEdgeTouched() {
        canDrag = true
        postDelayed(mPeekRunnable, 160)
    }

    private fun peekDrawer() {
        val childLeft = if (slide == Slide.LEFT) -mEdgeSize else mEdgeSize
        val scrollX = scrollX
        if (scrollX != 0 || childLeft == scrollX) {
            scroller.abortAnimation()
            return
        }
        scroller.startScroll(0, 0, scrollX + childLeft, 0, 400)
        invalidate()
        cancelChildViewTouch()
    }

    private fun cancelChildViewTouch() {
        // Cancel child touches
        if (!mChildrenCanceledTouch) {
            val now = SystemClock.uptimeMillis()
            val cancelEvent = MotionEvent.obtain(now, now,
                    MotionEvent.ACTION_CANCEL, 0.0f, 0.0f, 0)
            val childCount = childCount
            for (i in 0 until childCount) {
                getChildAt(i).dispatchTouchEvent(cancelEvent)
            }
            cancelEvent.recycle()
            mChildrenCanceledTouch = true
        }
    }

    override fun scrollTo(x1: Int, y1: Int) {
        var x = x1
        if (slide == Slide.LEFT) {
            if (x >= 0) {
                x = 0
                setSlideState(State.CLOSE)

            }
            if (x <= -slideWidth) {
                x = -slideWidth
                setSlideState(State.OPEN)
            }
        } else {
            if (x <= 0) {
                x = 0
                setSlideState(State.CLOSE)
            }
            if (x >= slideWidth) {
                x = slideWidth
                setSlideState(State.OPEN)
            }
        }
        foreground.alpha = abs(x) / 10
        if (!isFollowing) {
            ViewHelper.setTranslationX(contentView, x * 1f)
        } else {
            if (isAnim) {
                ViewHelper.setTranslationX(slideView!!, x * scale)
            }
        }
        if (scrollX != x) {
            super.scrollTo(x, y1)
        }
    }

    private fun eventUp() {
        val scrollX = scrollX
        val velocityTracker = mVelocityTracker
        velocityTracker!!.computeCurrentVelocity(1000)
        val velocityX = velocityTracker.xVelocity.toInt()
        if (slide == Slide.LEFT) {
            if (velocityX > SNAP_VELOCITY) {
                open()
            } else if (velocityX < -SNAP_VELOCITY) {
                close()
            } else {
                if (scrollX < -slideWidth / 2) {//打开
                    open()
                } else {
                    close()
                }
            }
        } else if (slide == Slide.RIGHT) {
            if (velocityX > SNAP_VELOCITY) {
                close()
            } else if (velocityX < -SNAP_VELOCITY) {
                open()
            } else {
                if (scrollX > slideWidth / 2) {//打开
                    open()
                } else {
                    close()
                }
            }

        }
    }

    fun open() {
        val scrollX = scrollX
        if (slide == Slide.LEFT) {
            scroller.startScroll(scrollX, 0, -(scrollX + slideWidth), 0, 400)
            invalidate()
        } else if (slide == Slide.RIGHT) {
            scroller.startScroll(scrollX, 0, slideWidth - scrollX, 0, 400)
            invalidate()
        }

    }

    fun close() {
        val scrollX = scrollX
        scroller.startScroll(scrollX, 0, -scrollX, 0, 400)
        invalidate()

    }

    private fun saveInitialMotion(x: Float, y: Float, pointerId: Int) {
        if (mInitialEdgesTouched == null || mInitialEdgesTouched?.size!! <= pointerId) {
            val iit = IntArray(pointerId + 1)
            mInitialEdgesTouched?.let {
                System.arraycopy(it, 0, iit, 0, it.size)
            }
            mInitialEdgesTouched = iit
        }
        mInitialEdgesTouched!![pointerId] = getEdgesTouched(x.toInt(), y.toInt())
    }

    //边缘检测
    private fun getEdgesTouched(x: Int, y: Int): Int {
        var result = 0
        if (x < left + mEdgeSize) result = result or EDGE_LEFT
        if (x > right - mEdgeSize) result = result or EDGE_RIGHT
        return result
    }


    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, 0)
            invalidate()
        }

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        contentView.layout(left, top, right, bottom)
        if (slide == Slide.LEFT) {
            slideView!!.layout(if (isAnim && isFollowing) -slideWidth + collapseOffset else -slideWidth, top, if (isAnim && isFollowing) collapseOffset else 0, bottom)
        } else {
            slideView!!.layout(right, top, right + slideWidth, bottom)
        }
        if (!isFollowing) {
            slideView.bringToFront()
        } else {
            contentView.bringToFront()
        }
        contentView.isClickable = true
    }

}
