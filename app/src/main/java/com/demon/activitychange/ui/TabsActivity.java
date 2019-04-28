package com.demon.activitychange.ui;


import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.demon.activitychange.R;
import com.demon.activitychange.ui.fragment.MyScriptFragment;
import com.demon.activitychange.ui.fragment.MarketFragment;
import com.demon.activitychange.ui.fragment.MyFragment;
import com.demon.activitychange.ui.presenter.TabPresenter;
import com.lixh.base.BaseActivity;
import com.lixh.view.LoadView;
import com.lixh.view.UToolBar;


/**
 * Created by LIXH on 2016/12/21.
 * email lixhVip9@163.com
 * des
 */
public class TabsActivity extends BaseActivity<TabPresenter> {


    @Override
    public boolean isShowBack() {
        return false;
    }

    @Override
    public boolean isDoubleExit() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initTitle(UToolBar toolBar) {

    }

    @Override
    public void initLoad(LoadView.Builder builder) {
        builder.swipeBack = false;
        builder.hasToolbar = false;
        builder.hasBottomBar = true;
        builder.addItem(new BottomNavigationItem(R.mipmap.ic_wrong, "我的脚本")
                        .setActiveColorResource(R.color.colorAccent)
                , new BottomNavigationItem(R.mipmap.ic_wrong, "脚本市场")
                        .setActiveColorResource(R.color.colorAccent)
                , new BottomNavigationItem(R.mipmap.ic_wrong, "我的")
                        .setActiveColorResource(R.color.colorAccent));
        builder.addFragment(new MyScriptFragment(), new MarketFragment(), new MyFragment());
        builder.setOnTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {

            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

}
