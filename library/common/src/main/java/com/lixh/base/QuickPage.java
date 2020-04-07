package com.lixh.base;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lixh.R;
import com.lixh.base.adapter.recycleview.DividerDecoration;
import com.lixh.utils.LoadingTip;
import com.lixh.view.refresh.SpringView;

import java.util.List;

/**
 * Created by LIXH on 2017/2/28.
 * email lixhVip9@163.com
 * des
 */

public class QuickPage<T> implements BaseQuickAdapter.RequestLoadMoreListener, SpringView.OnRefreshListener, LoadingTip.onReloadListener {
    View rootView;
    RecyclerView recyclerView;
    SpringView springView;
    private LoadingTip loadTip;
    int page = 0;
    int pageSize = 10;
    LoadingTip loadingTip;
    public BaseQuickAdapter mAdapters;
    boolean refresh;
    boolean pullLoadMore;
    boolean isAutoLoadMore;
    boolean isAutoRefresh;
    @DimenRes
    int divideHeight = R.dimen.space_2;
    private OnLoadingListener onLoadingListener;
    private RecyclerView.OnScrollListener onScrollListener;
    private int divideColor;
    RecyclerView.LayoutManager layoutManager;
    boolean isDefault = true;

    public static QuickPage with(Context context) {
        return new QuickPage(context);
    }

    public QuickPage(Context context) {
        mContext = context;
        rootView = inflate(R.layout.base_recyview);
        recyclerView = $(R.id.recycle);
        springView = $(R.id.springView);
        layoutManager = new LinearLayoutManager(context);
    }

    //是否应用默认的模板
    public QuickPage setEnabled(boolean isDefault) {
        this.isDefault = isDefault;
        return this;
    }

    public QuickPage bindRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        return this;
    }

    @Override
    public void reload() {
        onRefresh();
    }

    public QuickPage setAdapter(BaseQuickAdapter mAdapters) {
        this.mAdapters = mAdapters;
        return this;
    }


    public QuickPage setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        return this;
    }


    public void addData(List<T> datas) {
        if (mAdapters != null) {
            if (page == 1) {
                mAdapters.replaceData(datas);
            } else {
                mAdapters.addData(datas);
            }
            if (datas.size() < pageSize) {
                mAdapters.loadMoreEnd(page == 1);
            } else {
                mAdapters.loadMoreComplete();
            }
            if (page == 1 && isAutoLoadMore) {
                mAdapters.disableLoadMoreIfNotFullPage();
            }
        }
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        onLoad(onLoadingListener);
    }

    public void smoothScrollToPosition(int position) {
        if (recyclerView != null) {
            recyclerView.smoothScrollToPosition(position);
        }
    }


    public interface OnLoadingListener {
        void load(int page);
    }

    /**
     * @param loadStatus //状态
     */

    public void finish(@LoadingTip.LoadStatus int loadStatus) {
        onError(loadStatus);
    }

    /**
     * 结束填充
     */
    public void finish() {
        springView.finishRefreshAndLoadMore();
        onError(LoadingTip.LoadStatus.FINISH);
    }

    /**
     * @param loadStatus 结束状态
     */
    public void onError(@LoadingTip.LoadStatus int loadStatus) {
        switch (loadStatus) {
            case LoadingTip.LoadStatus.SHOW_LOAD_MORE_ERROR: //分页加载时
                if (page > 1) {
                    page--;
                }
                if (mAdapters != null) {
                    mAdapters.loadMoreFail();
                }
                break;
            default:
                if (loadingTip != null)
                    loadingTip.setLoadingTip(loadStatus);
                break;
        }

    }

    public void onLoad(OnLoadingListener onLoadingListener) {
        if (this.onLoadingListener != null) {
            this.onLoadingListener.load(page);
        }
    }

    @Override
    public void onRefresh() {
        page = 0;
        onLoad(onLoadingListener);
    }

    static Context mContext;

    public QuickPage build() {
        springView.setAutoRefresh(isAutoRefresh);
        onLoadingListener = getOnLoadingListener();
        if (loadingTip != null) {
            loadingTip.setLoadingTip(LoadingTip.LoadStatus.LOADING);
            loadingTip.setOnReloadListener(this);
        }
        recyclerView.setLayoutManager(layoutManager);

        if (onScrollListener != null) {
            recyclerView.addOnScrollListener(onScrollListener);
        }
        if (mAdapters != null) {
            if (isAutoLoadMore) {
                mAdapters.setEnableLoadMore(true);
            }
            recyclerView.setAdapter(mAdapters);
            if (isPullLoadMore()) {
                springView.setOnLoadListener(() -> onLoadMoreRequested());
            }
            if (isRefresh() && springView != null) {
                springView.setOnRefreshListener(this);
            } else {
                onRefresh();
            }

        }
        return this;
    }

    /**
     * Set the ItemDecoration to the recycler
     *
     * @param color
     * @param height
     * @param paddingLeft
     * @param paddingRight
     */

    public void setItemDecoration(int color, @DimenRes int height, int paddingLeft,
                                  int paddingRight) {
        recyclerView.addItemDecoration(new DividerDecoration(mContext, OrientationHelper.VERTICAL, height, color));
    }

    public <T extends View> T $(int viewId) {
        return (T) rootView.findViewById(viewId);
    }

    public View getRootView() {
        return rootView;
    }

    protected View inflate(int layoutResID) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(layoutResID, null);
        return view;
    }


    public QuickPage setLoadTip(LoadingTip loadTip) {
        this.loadTip = loadTip;
        return this;
    }


    public QuickPage setOnLoadingListener(OnLoadingListener onLoadingListener) {
        this.onLoadingListener = onLoadingListener;
        return this;
    }

    public QuickPage setAutoLoadMore(boolean autoLoadMore) {
        isAutoLoadMore = autoLoadMore;
        return this;
    }

    public QuickPage setAutoRefresh(boolean autoRefresh) {
        this.isAutoRefresh = autoRefresh;
        return this;
    }

    public boolean isPullLoadMore() {
        return pullLoadMore;
    }

    public QuickPage setPullLoadMore(boolean pullLoadMore) {
        this.pullLoadMore = pullLoadMore;
        return this;
    }

    public OnLoadingListener getOnLoadingListener() {
        return onLoadingListener;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public QuickPage setRefresh(boolean refresh) {
        this.refresh = refresh;
        return this;
    }

    @DimenRes
    public int getDivideHeight() {
        return divideHeight;
    }


    public QuickPage setDivideHeight(@DimenRes int divideHeight) {
        this.divideHeight = divideHeight;
        return this;
    }

    public QuickPage setDivideHeight(@DimenRes int divideHeight, @ColorRes int color) {
        this.divideHeight = divideHeight;
        divideColor = color;
        return this;
    }


    /**
     * recycleView 滚动监听
     *
     * @param onScrollListener
     * @return
     */
    public QuickPage addOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
        return this;
    }

}