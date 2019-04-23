package com.demon.activitychange;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.demon.activitychange.UiTools.GlobalView;
import com.demon.activitychange.adapter.ViewHolder;
import com.demon.activitychange.adapter.abslistview.CommonAdapter;
import com.demon.activitychange.bean.AppInfo;
import com.lixh.jsSdk.AccessibilityUtil;
import com.lixh.jsSdk.jscrawler.JsCrawler;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Intent intent;
    ListView listView;
    List<AppInfo> appInfoList = new ArrayList<>();
    CommonAdapter adapter;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.lv);
        tv = findViewById(R.id.tv_status);
        if (isServiceStart()) {
            GlobalView.init(this).showView(getPackageName() + "\n" + getClass().getCanonicalName());
            tv.setText("服务已开启");
        } else {
            tv.setText("服务未开启");
        }
        listView.setAdapter(adapter = new CommonAdapter<AppInfo>(this, R.layout.lv_item_app_info, appInfoList) {
            @Override
            public void convert(ViewHolder holder, AppInfo appInfo) {
                TextView projectName = holder.getView(R.id.pro_name);
                projectName.setText(appInfo.getPackageName());
            }
        });
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (!isServiceStart()) {
                AccessibilityUtil.goAccess(this);
            } else {
                tv.setText("服务已开启");
                intent = new Intent(MainActivity.this, ListeningService.class);
                intent.putExtra("appinfo", appInfoList.get(position));
                startService(intent);
            }
        });
        AppInfo appInfo = new AppInfo();
        appInfoList.add(appInfo);
        adapter.notifyDataSetChanged();
    }

    boolean isServiceStart() {
        return AccessibilityUtil.isAccessibilitySettingsOn(this, ListeningService.class.getCanonicalName());
    }
}
