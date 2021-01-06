package com.lixh.base


import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.lixh.R
import com.lixh.utils.LoadingTip
import com.lixh.utils.UView
import com.lixh.view.OnPageListener
import com.lixh.view.refresh.SpringView
import kotlinx.android.synthetic.main.base_recyview.view.*
import java.util.*

/**
 * Created by LIXH on 2017/2/28.
 * email lixhVip9@163.com
 * des
 */

class QuickPage(context: Context,
                private var isRefresh: Boolean = false,
                private var pullLoadMore: Boolean = false,
                private var isAutoLoadMore: Boolean = false,
                private var isAutoRefresh: Boolean = false,
                private var startPage: Int = 1,
                private var page: Int = startPage,
                private var tempPage: Int = startPage,
                private var pageSize: Int = 10,
                private var loadingTip: LoadingTip?,
                private var onPageListener: OnPageListener?,
                private var onScrollListener: RecyclerView.OnScrollListener?,
                private var mAdapters: BaseQuickAdapter<*, *>? = null
) : OnLoadMoreListener, SpringView.OnRefreshListener, LoadingTip.OnReloadListener {

    private var rootView: View = UView.inflate(context, R.layout.base_recyview)
    private var springView: SpringView = rootView.springView
    private var recyclerView: RecyclerView = rootView.recycle
    private var layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)

    init {
        recyclerView.layoutManager = layoutManager
        onScrollListener?.let { recyclerView.addOnScrollListener(it) }
        loadingTip?.let { tip ->
            tip.setLoadingTip(LoadingTip.LoadStatus.LOADING)
            tip.onReloadListener = this
        }

        mAdapters?.let {
            it.loadMoreModule.isEnableLoadMore = isAutoLoadMore
            recyclerView.adapter = it
            if (pullLoadMore) {
                springView.setOnLoadListener(object : SpringView.OnLoadListener {
                    override fun onLoad() {
                        onLoadMore()
                    }
                })

            } else if (isAutoLoadMore) {
                it.loadMoreModule.setOnLoadMoreListener(this)

            }
            springView.setAutoRefresh(isAutoRefresh)
            if (isRefresh) {
                springView.setOnRefreshListener(this)
            }
        }

    }

    override fun reload() {
        onRefresh()
    }


    fun addData(data: Collection<Nothing>) {
        mAdapters?.let {
            page = tempPage
            if (page == startPage) {
                it.setList(data)
            } else {
                it.addData(data)
            }
            if (data.size < pageSize) {
                it.loadMoreModule.loadMoreEnd(page == startPage)
            } else {
                it.loadMoreModule.loadMoreComplete()
            }
            if (page == startPage && isAutoLoadMore) {
                it.loadMoreModule.checkDisableLoadMoreIfNotFullPage()
            }
        }
    }

    /**
     * @param loadStatus //状态
     */

    fun finish(@LoadingTip.LoadStatus loadStatus: Int) {
        var status: Int = loadStatus

        //说明是第一页
        //说明是分页
        if (page == startPage) {
            mAdapters?.setList(ArrayList())
        } else {
            status = LoadingTip.LoadStatus.SHOW_LOAD_MORE_ERROR
        }
        when (status) {
            LoadingTip.LoadStatus.SHOW_LOAD_MORE_ERROR //分页加载时
            -> {
                mAdapters?.loadMoreModule?.loadMoreFail()
            }
            else -> loadingTip?.setLoadingTip(loadStatus)
        }

    }

    /**
     * 结束填充
     */
    fun finish() {
        springView.finishRefreshAndLoadMore()
        finish(LoadingTip.LoadStatus.FINISH)
    }


    override fun onLoadMore() {
        tempPage = page
        tempPage++
        onPageListener?.load(page)
    }

    override fun onRefresh() {
        tempPage = startPage
        page = tempPage
        finish(LoadingTip.LoadStatus.LOADING)
        onPageListener?.load(page)
    }
}