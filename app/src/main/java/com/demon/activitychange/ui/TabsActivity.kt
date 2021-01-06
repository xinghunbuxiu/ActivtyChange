package com.demon.activitychange.ui;


import android.os.Bundle
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.demon.activitychange.R
import com.demon.activitychange.ui.fragment.MarketFragment
import com.demon.activitychange.ui.fragment.MyFragment
import com.demon.activitychange.ui.fragment.MyScriptFragment
import com.lixh.base.BaseActivity


/**
 * Created by LIXH on 2016/12/21.
 * email lixhVip9@163.com
 * des
 */
class TabsActivity : BaseActivity(true, ui = {
    //带有底部导航栏
    bottomBar(fragments = arrayOf(MyScriptFragment(), MarketFragment(), MyFragment()),
            items = arrayOf(BottomNavigationItem(R.mipmap.ic_wrong, "我的脚本")
                    .setActiveColorResource(R.color.colorAccent), BottomNavigationItem(R.mipmap.ic_wrong, "脚本市场")
                    .setActiveColorResource(R.color.colorAccent), BottomNavigationItem(R.mipmap.ic_wrong, "我的")
                    .setActiveColorResource(R.color.colorAccent))) {}

}) {

    override fun init(savedInstanceState: Bundle?) {

    }
}
