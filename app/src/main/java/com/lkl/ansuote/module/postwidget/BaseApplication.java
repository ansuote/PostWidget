package com.lkl.ansuote.module.postwidget;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * Created by huangdongqiang on 27/09/2017.
 */
public class BaseApplication extends Application {
    private static BaseApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        //初始化工具类
        Utils.init(this);
    }

    public static BaseApplication getInstance() {
        return INSTANCE;
    }
}
