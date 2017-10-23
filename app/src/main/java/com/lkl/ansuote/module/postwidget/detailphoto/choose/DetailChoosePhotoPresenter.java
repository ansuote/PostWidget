package com.lkl.ansuote.module.postwidget.detailphoto.choose;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lkl.ansuote.hdqlibrary.mvp.BasePresenter;
import com.lkl.ansuote.hdqlibrary.util.Utils;
import com.lkl.ansuote.module.postwidget.BaseApplication;
import com.lkl.ansuote.module.postwidget.base.Constants;
import com.lkl.ansuote.module.postwidget.base.entity.ImageEntity;
import com.lkl.ansuote.module.postwidget.detailphoto.choose.view.IDetailChoosePhotoView;

import java.util.List;

/**
 * Created by huangdongqiang on 09/10/2017.
 */
public class DetailChoosePhotoPresenter extends BasePresenter<IDetailChoosePhotoView> {
    private List<ImageEntity> mAllList; //当前层级下所有图片
    private int mPosition;  //当前显示的页码
    private int mDirPhotoCount; //该目录下所有图片的数量
    private boolean mHideTopAndBottomLayout;    //是否隐藏头部布局和底部布局
    private int mCurrentChooseCount; //当前选中的数量
    private int mMaxChooseCount;    //最大选中数
    private List<ImageEntity> mSelectedList;

    @Override
    public void initVariables(Bundle savedInstanceState, Intent intent) {
        if (null != intent) {
            mPosition = intent.getIntExtra(Constants.EXTRA_PHOTO_CLICK_POSITION, 0);
            //mAllList = intent.getParcelableArrayListExtra(Constants.EXTRA_PHOTO_LIST);
            //mSelectedList = intent.getParcelableArrayListExtra(Constants.EXTRA_PHOTO_SELECT_LIST);

            mAllList = (List<ImageEntity>) Utils.readObjFromFile(Utils.getFilePath(BaseApplication.getInstance(), Constants.PostWidget.FILE_NAME_PHOTO_ALL_LIST));
            mSelectedList = (List<ImageEntity>) Utils.readObjFromFile(Utils.getFilePath(BaseApplication.getInstance(), Constants.PostWidget.FILE_NAME_PHOTO_SELECT_LIST));

            mCurrentChooseCount = intent.getIntExtra(Constants.EXTRA_PHOTO_CURRENT_CHOOSE_COUNT, 0);
            mMaxChooseCount = intent.getIntExtra(Constants.EXTRA_PHOTO_MAX_CHOOSE_COUNT, 0);
            if (null != mAllList) {
                mDirPhotoCount = mAllList.size();
            }
        }
    }

    @Override
    public void onStart() {
        if (!isViewAttached()) {
            return;
        }

        getView().initDetailPhotoAdapter(mAllList, mPosition);
        getView().setTextCount(mPosition + 1, mDirPhotoCount);
        getView().setChooseCount(mCurrentChooseCount, mMaxChooseCount);
        setCheckBox(mPosition);
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

        reverseShowMode();
    }


    /**
     * 进入预览模式/退出预览模式
     */
    public void reverseShowMode() {
        mHideTopAndBottomLayout = !mHideTopAndBottomLayout;

        if (mHideTopAndBottomLayout) {
            getView().hideBottomLayout();
            getView().hideTopLayout();
        } else {
            getView().showBottomLayout();
            getView().showTopLayout();
        }
    }


    public void onPageSelected(int position) {
        if (!isViewAttached()) {
            return;
        }

        //重置当前页码
        mPosition = position;

        getView().setTextCount(position + 1, mDirPhotoCount);

        setCheckBox(position);
    }

    /**
     * 设置是否选中
     * @param position
     */
    private void setCheckBox(int position) {
        if (null != mAllList && mAllList.size() > position) {
            ImageEntity entity = mAllList.get(position);
            if (null != entity) {
                boolean isChecked = entity.isChecked();
                getView().setCheckBox(isChecked);
            }
        }
    }

    public void clickCheckBox() {
        if (!isViewAttached()) {
            return;
        }

        if (null != mAllList && mAllList.size() > mPosition) {
            ImageEntity entity = mAllList.get(mPosition);
            if (null != entity) {
                boolean isChecked = entity.isChecked();

                if (isChecked) {
                    mCurrentChooseCount--;
                    entity.setChecked(false);
                    getView().setCheckBox(false);
                } else {
                    if (isLegalChooseCount()) {
                        mCurrentChooseCount++;
                        entity.setChecked(true);
                        getView().setCheckBox(true);
                    } else {
                        getView().setCheckBox(false);
                        getView().showOverMaxChooseCountThoast();
                    }
                }

                getView().setChooseCount(mCurrentChooseCount, mMaxChooseCount);
            }
        }

    }

    /**
     * 判断是否是合法的选中数
     * @return
     */
    public boolean isLegalChooseCount() {
        return mCurrentChooseCount < mMaxChooseCount;
    }

    public void clickDoneBtn() {

        //判断该目录下的数据，选中情况是否发生改变
        if (null == mSelectedList || null == mAllList) {
            return;
        }

        //遍历当前层级的所有数据
        for (ImageEntity entity : mAllList) {
            boolean isChecked = entity.isChecked();
            String path = entity.getPath();
            boolean isFound = false;
            //遍历选中集合的数据（涉及不同层级）
            for (ImageEntity selectedEntity : mSelectedList) {
                String selectedPath = selectedEntity.getPath();
                if (path.equals(selectedPath)) {
                    selectedEntity.setChecked(isChecked);
                    isFound = true;
                    break;
                }
            }

            //如果遍历完，没有找到该元素，并且该元素是打勾的，则加入到选中集合中
            if (!isFound && isChecked) {
                mSelectedList.add(entity);
            }
        }

        if (null != mSelectedList && mSelectedList.size() > 0) {
            getView().startAddPhotoActivity(mSelectedList);
            getView().finishCurrentActivity();
        } else {
            getView().showChoosePhotoToast();
        }
    }
}
