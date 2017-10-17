package com.lkl.ansuote.module.postwidget.detailphoto.delete;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lkl.ansuote.hdqlibrary.mvp.BasePresenter;
import com.lkl.ansuote.module.postwidget.base.Constants;
import com.lkl.ansuote.module.postwidget.base.entity.ImageEntity;
import com.lkl.ansuote.module.postwidget.detailphoto.delete.view.IDetailDeletePhotoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangdongqiang on 16/10/2017.
 */
public class DetaiDetailPhotoPresenter extends BasePresenter<IDetailDeletePhotoView> {
    private int mPosition;  //当前显示的页码
    private List<ImageEntity> mAllList; //当前层级下所有图片
    private int mDirPhotoCount;

    @Override
    public void initVariables(Bundle savedInstanceState, Intent intent) {
        if (null != intent) {
            mPosition = intent.getIntExtra(Constants.EXTRA_PHOTO_CLICK_POSITION, 0);
            mAllList = intent.getParcelableArrayListExtra(Constants.EXTRA_PHOTO_LIST);
            mDirPhotoCount = mAllList.size();
        }
    }

    @Override
    public void onStart() {
        if (!isViewAttached()) {
            return;
        }

        getView().initDetailPhotoAdapter(mAllList, mPosition);
        getView().setTextCount(mPosition + 1, mDirPhotoCount);
    }

    /**
     * 点击图片
     * @param v
     * @param position
     * @param entity
     */
    public void onClickPhotoViewListener(View v, int position, ImageEntity entity) {
        if (!isViewAttached()) {
            return;
        }

        onBackPressed();

    }

    public void onPageSelected(int position) {
        if (!isViewAttached()) {
            return;
        }

        //重置当前页码
        mPosition = position;

        getView().setTextCount(position + 1, mDirPhotoCount);

    }

    public void deletePhoto() {
        if (!isViewAttached()) {
            return;
        }

        ArrayList<ImageEntity> list = new ArrayList<>();
        if (null != list) {
            list.addAll(mAllList);
            list.remove(mPosition);
            mDirPhotoCount = list.size();
            int oldDirPhotoCount = mAllList.size();
            mAllList = list;
            if (mDirPhotoCount > 0) {
                //如果当前选中的是最后一个下标则当前的位置要减1
                if (mPosition == oldDirPhotoCount - 1) {
                    mPosition--;
                }

                getView().initDetailPhotoAdapter(list, mPosition);
                getView().setTextCount(mPosition + 1, mDirPhotoCount);
            } else {
                onBackPressed();
            }
        }
    }

    public void onBackPressed() {
        getView().startAddPhotoActivity(mAllList);
        getView().finishCurrentActivity();
    }
}
