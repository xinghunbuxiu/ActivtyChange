package com.demon.activitychange.ui.fragment;

import android.graphics.Color;

import com.demon.activitychange.ui.presenter.MyScriptPresenter;
import com.lixh.base.BaseFragment;
import com.lixh.view.UToolBar;


public class MyScriptFragment extends BaseFragment<MyScriptPresenter> {

    @Override
    public void initTitle(UToolBar toolBar) {
        toolBar.setTitle("小爱");
        toolBar.setTitleTextColor(Color.WHITE);
    }

    @Override
    public int getLayoutId() {
        return 0;
    }
}
