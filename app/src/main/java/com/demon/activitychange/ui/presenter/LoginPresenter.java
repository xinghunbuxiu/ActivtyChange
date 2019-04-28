package com.demon.activitychange.ui.presenter;

import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.demon.activitychange.ui.api.Api;
import com.demon.activitychange.ui.api.ApiService;
import com.lixh.presenter.BasePresenter;
import com.lixh.rxhttp.RxSubscriber;
import com.lixh.utils.UToast;

import java.util.Map;


public class LoginPresenter extends BasePresenter {


    ApiService apiService = Api.getDefault();

    @Override
    public void onCreate(Bundle savedInstanceState) {
    }

    /**
     * 登录
     */
    public void login(String username, String password, String imei) {
//        rxHelper.createSubscriber();
    }

    /**
     * 注册
     */
    public void register(String username, String password) {
//        rxHelper.createSubscriber(apiService.register(username, password)
//                , new RxSubscriber<String>(activity, true) {
//                    @Override
//                    protected void _onNext(String bean) {
//                        UToast.showShort("注册成功,当前身份：体验会员");
//                        login(username, password, LocalAppInfo.getLocalAppInfo().getIMei());
//                    }
//                });
    }

    /**
     * 找回密码或者修改密码
     */
    public void forgetPassword(Map<String, String> param) {
    }


}
