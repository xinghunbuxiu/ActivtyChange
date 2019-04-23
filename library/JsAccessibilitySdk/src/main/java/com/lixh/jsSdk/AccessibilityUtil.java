package com.lixh.jsSdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by LIXH on 2019/4/18.
 * email lixhVip9@163.com
 * des
 */
public class AccessibilityUtil {
    public static final String TAG = "AccessibilityUtil";

    /**
     * 检测辅助功能是否开启
     *
     * @param mContext
     * @return boolean
     */
    public static boolean isAccessibilitySettingsOn(Context mContext, String serviceName) {
        int accessibilityEnabled = 0;
        // 对应的服务
        final String service = mContext.getPackageName() + "/" + serviceName;
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
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

    /**
     * 前往开启辅助服务界面
     */
    public static void goAccess(Context mContext) {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public static void JumpToOtherApp(Context context, String packageName, String mainName) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ComponentName cn = new ComponentName(packageName, mainName);
        intent.setComponent(cn);
        if (intent.resolveActivityInfo(context.getPackageManager(), PackageManager.MATCH_DEFAULT_ONLY) != null) {//启动的intent存在
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "应用未安装", Toast.LENGTH_SHORT).show();
        }
    }
}
