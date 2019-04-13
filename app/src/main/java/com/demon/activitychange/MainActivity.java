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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean isServiceStart = isAccessibilitySettingsOn(this, ListeningService.class.getCanonicalName());
        if (isServiceStart) {
            GlobalView.init(this).showView(getPackageName() + "\n" + getClass().getCanonicalName());
        }
        findViewById(R.id.start).setOnClickListener(view -> {
            if (!isServiceStart) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
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


    /**
     * 检测辅助功能是否开启
     *
     * @param mContext
     * @return boolean
     */
    private boolean isAccessibilitySettingsOn(Context mContext, String serviceName) {
        int accessibilityEnabled = 0;
        // 对应的服务
        final String service = getPackageName() + "/" + serviceName;
        try {
            accessibilityEnabled = Secure.getInt(mContext.getApplicationContext().getContentResolver(),
                    Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
        }
        SimpleStringSplitter mStringColonSplitter = new SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue = Secure.getString(mContext.getApplicationContext().getContentResolver(),
                    Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    Log.v(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILITY IS DISABLED***");
        }
        return false;
    }
}
