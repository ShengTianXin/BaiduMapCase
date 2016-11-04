package com.feicui.demo.baidumapcase;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by Administrator on 2016/11/3 0003.
 */
public class BaiduApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        // 对SDK进行初始化，并在清单文件里注册
        SDKInitializer.initialize(getApplicationContext());
    }
}
