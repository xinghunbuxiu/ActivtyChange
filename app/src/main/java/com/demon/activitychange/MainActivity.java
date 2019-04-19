package com.demon.activitychange;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils.SimpleStringSplitter;
import android.util.Log;

import com.demon.UiTools.GlobalView;
import com.lixh.jsSdk.AccessibilityUtil;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean isServiceStart = AccessibilityUtil.isAccessibilitySettingsOn(this, ListeningService.class.getCanonicalName());
        if (isServiceStart) {
            GlobalView.init(this).showView(getPackageName() + "\n" + getClass().getCanonicalName());
        }
        findViewById(R.id.start).setOnClickListener(view -> {
            if (!isServiceStart) {
                AccessibilityUtil.goAccess(this);
            } else {
                intent = new Intent(MainActivity.this, ListeningService.class);
                startService(intent);
            }
        });

        findViewById(R.id.stop).setOnClickListener(v -> {
            if (intent != null) {
                stopService(intent);
            }
        });
    }



}
