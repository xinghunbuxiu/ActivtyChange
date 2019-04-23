package com.demon.activitychange.UiTools;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.demon.activitychange.R;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.Util;

/**
 * Created by LIXH on 2019/4/8.
 * email lixhVip9@163.com
 * des
 */
public class GlobalView {
    private View mView;
    Context mContext;
    static GlobalView globalView;

    private GlobalView(Context context) {
        this.mContext = context;
        if (mView == null) {
            mView = Util.inflate(context, R.layout.item_history);
        }
        FloatWindow.with(context)
                .setView(mView)
                .setWidth(Screen.width, 0.6f)
                .setHeight(Screen.width, 0.4f)
                .setMoveType(MoveType.active)
                .setDesktopShow(true)
                .build();
    }


    public static GlobalView init(Context context) {
        if (globalView == null) {
            globalView = new GlobalView(context);
        }
        return globalView;
    }

    public void showView(String s) {
        Log.e("service", s);
        ((TextView) mView.findViewById(R.id.historyWindowName)).setText(s);
        FloatWindow.get().show();
    }

    public void removeView() {
        FloatWindow.get().hide();
    }
}
