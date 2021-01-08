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
        return mChildView.canScrollVertically(-1)
    }

    fun canChildScrollDown(mChildView: View?): Boolean {
        if (mChildView == null) {
            return false
        }
        return mChildView.canScrollVertically(1)
    }

}