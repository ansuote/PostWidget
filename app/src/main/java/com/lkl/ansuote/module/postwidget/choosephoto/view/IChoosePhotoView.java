package com.lkl.ansuote.module.postwidget.choosephoto.view;

import com.lkl.ansuote.module.postwidget.base.entity.ImageDirEntity;
import com.lkl.ansuote.module.postwidget.base.entity.ImageEntity;

import java.util.List;

/**
 * Created by huangdongqiang on 27/09/2017.
 */
public interface IChoosePhotoView {
    /**
     * 检查存储器访问权限
     * @return
     */
    void checkStoragePermission();

    /**
     * 刷新列表
     * @param list
     */
    void refreshRecyclerView(List<ImageEntity> list);

    /**
     * 显示目录选择的弹框
     * @param list
     * @param selectDir
     */
    void showPopupWindow(List<ImageDirEntity> list, ImageDirEntity selectDir);

    /**
     * 设置当前页面显示的图片数量
     * @param count
     */
    void setImageCount(int count);

    /**
     * 设置目录名称
     * @param text
     */
    void setDirText(String text);

    /**
     * 显示图片详情的界面
     * @param allList   数据集合
     * @param position  当前点击的数据对应的下标
     * @param currentChooseCount 当前选中的数量
     * @param maxChooseCont 最大选中数
     * @param selectedList 选中的最大数
     */
    void showDetailPhotoActivity(List<ImageEntity> allList,
                                 int position,
                                 int currentChooseCount,
                                 int maxChooseCont,
                                 List<ImageEntity> selectedList);

    /**
     * 设置完成按钮的文本
     * @param currentCount  当前选择的数量
     * @param maxCount      最大可以选择的数量
     */
    void setDoneBtnText(int currentCount, int maxCount);

    /**
     * 显示超过最大选择数的提示
     */
    void showOverMaxChooseCountThoast();

    /**
     * 获取图片图片的目录名称
     * @return
     */
    String getAllPictureDirName();

    /**
     * 开启选择图片，增加图片的界面
     * @param selectedList
     */
    void startAddPhotoActivity(List<ImageEntity> selectedList);

    void finishCurrentActivity();

    /**
     * 显示至少选中一张图片的提示
     */
    void showChoosePhotoToast();
}
