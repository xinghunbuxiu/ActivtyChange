package com.demon.activitychange.ui.presenter;

import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.lixh.presenter.BasePresenter;
import com.lixh.rxhttp.RxSubscriber;
import com.vipvideo.api.Api;
import com.vipvideo.api.HostType;
import com.vipvideo.bean.SearchVideoInfo;
import com.vipvideo.ui.ChannelActivity;

/**
 * Created by LIXH on 2018/11/16.
 * email lixhVip9@163.com
 * des
 */

public class MyScriptPresenter extends BasePresenter {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    }


    public void loadPageInfo(String searchName, int page) {
        ChannelActivity activity = getActivity();
        rxHelper.createSubscriber(Api.getDefault(HostType.M_MAHUA_URL).searchVideoInfo(page, 10, searchName, 1), new RxSubscriber<String>(activity, false) {
            @Override
            protected void _onNext(String result) {
                SearchVideoInfo videoInfo = JSON.parseObject(result, SearchVideoInfo.class);
                activity.setVideoInfoDatas(videoInfo.getData());
            }
        });
    }


}
