package com.lkl.ansuote.module.postwidget;

import android.app.Application;
import android.graphics.Bitmap;

import com.blankj.utilcode.util.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by huangdongqiang on 27/09/2017.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 设置图片加载Imageloader参数
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                //.showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true).cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options).build();
        ImageLoader.getInstance().init(config);

        //初始化工具类
        Utils.init(this);
    }
}
