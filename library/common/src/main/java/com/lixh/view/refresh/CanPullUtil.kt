package com.lixh.view.refresh

import android.os.Build
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.AbsListView


/**
 * Created by ybao on 16/3/7.
 */
object CanPullUtil {
    /**
     * * @param 负数表示检测上滑，正数表示下滑
     *
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child com.angel.recycling.desewang.view is a custom com.angel.recycling.desewang.view.
     */
    fun canChildScrollUp(mChildView: View?): Boolean {
        if (mChildView == null) {
            return false
        }
        if (Build.VERSION.SDK_INT < 14) {
            if (mChildView is AbsListView) {
                val absListView = mChildView as AbsListView?
                return absListView!!.childCount > 0 && (absListView.firstVisiblePosition > 0 || absListView.getChildAt(0)
                        .top < absListView.paddingTop)
            } else {
                return mChildView.canScrollVertically(-1) || mChildView.scrollY > 0
            }
        } else {
            return mChildView.canScrollVertically(-1)
        }
    }

    fun canChildScrollDown(mChildView: View?): Boolean {
        if (mChildView == null) {
            return false
        }
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mChildView is AbsListView) {
                val absListView = mChildView as AbsListView?
                if (absListView!!.childCount > 0) {
                    val lastChildBottom = absListView.getChildAt(absListView.childCount - 1).bottom
                    return absListView.lastVisiblePosition == absListView.adapter.count - 1 && lastChildBottom <= absListView.measuredHeight
                } else {
                    return false
                }

            } else {
                return mChildView.canScrollVertically(1) || mChildView.scrollY > 0
            }
        } else {
            return mChildView.canScrollVertically(1)
        }
    }

    fun addScrollListener(mChildView: View, pull: SpringView) {

        if (mChildView is RecyclerView) {

            mChildView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                internal var isSlidingToLast = false
                internal val linearLayoutManager: LinearLayoutManager = mChildView.layoutManager as LinearLayoutManager?

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    //                    int totalItemCount = linearLayoutManager.getItemCount();
                    //                    int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    //                    if ((newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_SETTLING)) {
                    //                        if (isSlidingToLast && totalItemCount <= (lastVisibleItem + 1) && recyclerView.canScrollVertically(-1)) {
                    //                            RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
                    //                            if (adapter instanceof RecyclerArrayAdapter) {
                    //                                ((RecyclerArrayAdapter) adapter).resumeMore();
                    //                            }
                    //                        }
                    //                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    isSlidingToLast = dy > 0
                }
            })
        }
    }

}