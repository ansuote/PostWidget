package com.lkl.ansuote.module.postwidget.addPhoto.view;

import com.lkl.ansuote.module.postwidget.addPhoto.view.postaddimage.entity.PostAddImageEntity;
import com.lkl.ansuote.module.postwidget.base.entity.ImageEntity;

import java.util.List;

/**
 * Created by huangdongqiang on 13/10/2017.
 */
public interface IAddPhotoView {

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
     * 显示出现选择图片的对话框
     */
    void showChoosePhotoDialog();

    /**
     * 显示选择图片的界面
     * @param maxChooseCount
     */
    void startChoosePhotoActivity( int maxChooseCount);

    /**
     * 开启图片详情轮播界面
     * @param allList   数据集
     * @param position  当前点击的位置
     */
    void startDetailDeletePhotoActivity(List<ImageEntity> allList, int position);

    /**
     * 提示必须要选中图片
     */
    void showMustChoosePhotoToast();

    /**
     * 获取输入内容
     * @return
     */
    String getEidtText();

    /**
     * 显示退出对话框
     */
    void showEixtDialog();
}
