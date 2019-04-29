package com.demon.activitychange.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.demon.activitychange.R;
import com.demon.activitychange.UiTools.GlobalView;
import com.demon.activitychange.bean.AppInfo;
import com.demon.activitychange.server.ListeningService;
import com.lixh.jsSdk.AccessibilityUtil;
import com.lixh.jsSdk.ZipUtils;
import com.lixh.utils.UFile;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Intent intent;
    EditText filepath;
    Button begin;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        filepath = findViewById(R.id.filePath);
        begin = findViewById(R.id.begin);
        tv = findViewById(R.id.tv_status);
        AppInfo appInfo = new AppInfo();

        if (isServiceStart()) {
            GlobalView.init(this).showView(getPackageName() + "\n" + getClass().getCanonicalName());
            tv.setText("服务已开启");
        } else {
            tv.setText("服务未开启");
        }
        begin.setOnClickListener((v) -> {
//            String file = "wechart.zip";
//            try {
//                InputStream inputStream = getAssets().open(file);
//                ZipUtils.Unzip(inputStream, UFile.getCacheDir());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            if (!isServiceStart()) {
                AccessibilityUtil.goAccess(this);
            } else {
                tv.setText("服务已开启");
                intent = new Intent(MainActivity.this, ListeningService.class);
                intent.putExtra("appinfo", appInfo);
                startService(intent);
            }
        });
    }

    boolean isServiceStart() {
        return AccessibilityUtil.isAccessibilitySettingsOn(this, ListeningService.class.getCanonicalName());
    }
}
