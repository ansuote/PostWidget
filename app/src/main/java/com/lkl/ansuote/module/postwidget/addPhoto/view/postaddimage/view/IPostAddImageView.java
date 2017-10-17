package com.lkl.ansuote.module.postwidget.addPhoto.view.postaddimage.view;


import com.lkl.ansuote.module.postwidget.addPhoto.view.postaddimage.entity.PostAddImageEntity;

import java.util.List;

/**
 * Created by huangdongqiang on 12/09/2017.
 */
public interface IPostAddImageView {

/*    *//**
     * 设置最大的图片选择数
      * @param maxChooseCount
     *//*
    void setMaxChooseCount(int maxChooseCount);

    *//**
     * 设置列与列间的间距
     * @param margin
     *//*
    void setColumnMargin(int margin);

    *//**
     * 设置行与行之间的间距
     * @param margin
     *//*
    void setRowMargin(int margin);

    *//**
     * 设置一行有多少个
     * @param column
     *//*
    void setColumn(int column);*/

    /**
     * 根据选择图片数量，加载对应的界面
     * @param list
     */
    void refresh(List<PostAddImageEntity> list);

    /**
     * 获取当前选择的图片数量
     */
    int getCurrentChooseCount();

    /**
     * 手动销毁方法
     */
    void onDestroy();
}

