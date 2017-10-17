package com.lkl.ansuote.module.postwidget.addPhoto.view.postaddimage.view;


import com.lkl.ansuote.module.postwidget.addPhoto.view.postaddimage.entity.PostAddImageEntity;

/**
 * Created by huangdongqiang on 13/09/2017.
 */
public interface OnPostAddImageClickListener {

    /**
     * 点击图片
     */
    void onClickImage(int position, PostAddImageEntity entity);

    /**
     * 点击增加图片按钮
     */
    void onClickAddBtn();
}
