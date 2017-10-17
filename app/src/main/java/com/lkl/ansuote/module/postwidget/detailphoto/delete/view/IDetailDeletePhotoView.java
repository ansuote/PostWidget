package com.lkl.ansuote.module.postwidget.detailphoto.delete.view;

import com.lkl.ansuote.module.postwidget.base.entity.ImageEntity;

import java.util.List;

/**
 * Created by huangdongqiang on 16/10/2017.
 */
public interface IDetailDeletePhotoView {

    void initDetailPhotoAdapter(List<ImageEntity> list, int currentItem);

    /**
     * 设置数量（布局底部剧中处）
     * @param position  当前位置
     * @param maxChooseCount 总数量
     */
    void setTextCount(int position, int maxChooseCount);

    void finishCurrentActivity();

    /**
     * 开启选择图片，增加图片的界面
     */
    void startAddPhotoActivity(List<ImageEntity> selectedList);

    void showDeletePhotoDialog();
}
