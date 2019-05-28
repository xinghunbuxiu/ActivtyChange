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
import com.lixh.utils.PermissionUtils;
import com.lixh.utils.UFile;
import com.lixh.utils.UToast;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Intent intent;
    EditText filepath;
    Button begin;
    Button select;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        filepath = findViewById (R.id.filePath);
        begin = findViewById (R.id.begin);
        tv = findViewById (R.id.tv_status);
        select = findViewById (R.id.bt_select_file);
        AppInfo appInfo = new AppInfo ( );

        if (isServiceStart ( )) {
            GlobalView.init (this).showView (getPackageName ( ) + "\n" + getClass ( ).getCanonicalName ( ));
            tv.setText ("服务已开启");
        } else {
            tv.setText ("服务未开启");
        }
        select.setOnClickListener (v -> {
            String file = "wechart.zip";
            PermissionUtils.externalStorage (getApplication ( ), new PermissionUtils.RequestPermission ( ) {
                @Override
                public void onRequestPermissionFailure(List<String> list) {
                    // 请求失败回收当前服务
                    UToast.showShort ("没有SD卡储存权限,下载失败");

                }

                @Override
                public void onRequestPermissionSuccess() {
                    String saveFile = UFile.getCacheDir ( );
                    filepath.setText (saveFile);
                    try {
                        InputStream inputStream = getAssets ( ).open (file);
                        ZipUtils.Unzip (inputStream, saveFile);
                    } catch (IOException e) {
                        e.printStackTrace ( );
                    }
                }
            });

        });
        begin.setOnClickListener ((v) -> {
            if (!isServiceStart ( )) {
                AccessibilityUtil.goAccess (this);
            } else {
                tv.setText ("服务已开启");
                intent = new Intent (MainActivity.this, ListeningService.class);
                intent.putExtra ("appinfo", appInfo);
                startService (intent);
            }
        });
    }

    boolean isServiceStart() {
        return AccessibilityUtil.isAccessibilitySettingsOn (this, ListeningService.class.getCanonicalName ( ));
    }
}
