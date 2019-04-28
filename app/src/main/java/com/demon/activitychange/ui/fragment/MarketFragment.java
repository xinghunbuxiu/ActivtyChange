package com.demon.activitychange.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.demon.activitychange.R;
import com.demon.activitychange.ui.presenter.MarketPresenter;
import com.lixh.base.BaseFragment;
import com.lixh.base.adapter.recycleview.PageView;
import com.lixh.view.UToolBar;


public class MarketFragment extends BaseFragment<MarketPresenter> {
    PageView page;

    @Override
    public void initTitle(UToolBar toolBar) {
        toolBar.setVisibility(View.GONE);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initVLayout();
        page.onRefresh();
    }

    PageView.OnLoadingListener onLoadingListener = (page, onLoadFinish) -> mPresenter.getScriptList();

    private void initVLayout() {
        page = PageView.with(activity)
                .setPullLoadMore(true)
                .setRefresh(true)
                .setDivideHeight(R.dimen.space_1)
                .setLoadTip(tip)
                .setOnLoadingListener(onLoadingListener)
                .setAutoRefresh(false)
                .setMaxRecycledViews(0, 20)
                .build();
        layout.setContentView(page.getRootView());
    }


    @Override
    public int getLayoutId() {
        return 0;
    }

}
