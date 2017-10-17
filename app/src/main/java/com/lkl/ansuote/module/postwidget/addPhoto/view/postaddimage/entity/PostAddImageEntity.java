package com.lkl.ansuote.module.postwidget.addPhoto.view.postaddimage.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by huangdongqiang on 13/09/2017.
 */
public class PostAddImageEntity implements MultiItemEntity{
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_BUTTON_ADD = 1;

    //private int mPosition;

    private String mUrl;    //对应的本地路径／服务端 url

    private int mItemType;  //类型

    public PostAddImageEntity(int itemType){
        mItemType = itemType;
    }

    /*public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }*/

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    /*public void setItemType(int itemType) {
        mItemType = itemType;
    }*/

    @Override
    public int getItemType() {
        return mItemType;
    }
}
