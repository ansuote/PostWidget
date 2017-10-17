package com.lkl.ansuote.module.postwidget.detailphoto.choose.view;


import com.lkl.ansuote.module.postwidget.base.entity.ImageEntity;

import java.util.List;

/**
 * Created by huangdongqiang on 09/10/2017.
 */
public interface IDetailChoosePhotoView {
    void initDetailPhotoAdapter(List<ImageEntity> list, int currentItem);

    /**
     * 设置数量（布局右下角处）
     * @param position  当前位置
     * @param maxChooseCount 总数量
     */
    void setTextCount(int position, int maxChooseCount);

    /**
     * 设置选中图片数量（布局右上角处）
     * @param currentChooseCount
     * @param maxChooseCount
     */
    void setChooseCount(int currentChooseCount, int maxChooseCount);

    /**
     * 显示标题布局
     */
    void showTopLayout();

    /**
     * 隐藏标题布局
     */
    void hideTopLayout();

    /**
     * 显示底部布局
     */
    void showBottomLayout();

    /**
     * 隐藏底部布局
     */
    void hideBottomLayout();


    void setCheckBox(boolean b);

    /**
     * 显示超过最大选择数的提示
     */
    void showOverMaxChooseCountThoast();

    /**
     * 开启选择图片，增加图片的界面
     * @param selectedList
     */
    void startAddPhotoActivity(List<ImageEntity> selectedList);

    void finishCurrentActivity();
}
