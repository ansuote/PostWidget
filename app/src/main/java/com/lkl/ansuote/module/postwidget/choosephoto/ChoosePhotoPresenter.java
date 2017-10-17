package com.lkl.ansuote.module.postwidget.choosephoto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CheckBox;

import com.lkl.ansuote.hdqlibrary.mvp.BasePresenter;
import com.lkl.ansuote.module.postwidget.base.Constants;
import com.lkl.ansuote.module.postwidget.base.entity.ImageDirEntity;
import com.lkl.ansuote.module.postwidget.base.entity.ImageEntity;
import com.lkl.ansuote.module.postwidget.base.util.MediaUtil;
import com.lkl.ansuote.module.postwidget.choosephoto.view.IChoosePhotoView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huangdongqiang on 27/09/2017.
 */
public class ChoosePhotoPresenter extends BasePresenter<IChoosePhotoView>{
    private static final String TAG = "ChoosePhotoPresenter";
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private List<ImageEntity> mAllImageList;    //所有图片数据
    private List<ImageDirEntity> mAllImageDirList;//分类好的图片数据
    private ImageDirEntity mSelectedDirEntity;     //选中的目录数据
    private List<ImageEntity> mSelectedList = new ArrayList<>();  //选中的图片数据
    private final int DEFAULT_MAX_CHOOSE_COUNT = 9;    //默认的最大选择数量
    private final int DEFAULT_CURRENT_CHOOSE_COUNT = 0; //默认选中数量
    private int mMaxChooseCount;    //最大选择的数量
    private int mCurrentChooseCount;    //当前已经选中的数量
    private String mDefaultAllPictureDirName;   //所有图片的情况下，界面右下角显示的文字

    @Override
    public void initVariables(Bundle savedInstanceState, Intent intent) {
        if (null != intent) {
            mMaxChooseCount = intent.getIntExtra(Constants.EXTRA_PHOTO_MAX_CHOOSE_COUNT, DEFAULT_MAX_CHOOSE_COUNT);
            //mCurrentChooseCount = intent.getIntExtra(Constants.EXTRA_PHOTO_CURRENT_CHOOSE_COUNT, DEFAULT_CURRENT_CHOOSE_COUNT);
        }
    }

    @Override
    public void onStart() {
        if (!isViewAttached()) {
            return;
        }
        mDefaultAllPictureDirName = getView().getAllPictureDirName();
        getView().checkStoragePermission();
    }

    /**
     * 获取主存媒体资源中，所有图片的集合
     */
    public void getMediaImageList() {
        if (!isViewAttached()) {
            return;
        }
        mDisposable.add(Observable.just("getMediaImageList")
                //在io线程发射数据
                .subscribeOn(Schedulers.io())
                //访问前，弹出对话框等操作
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        if (isViewAttached()) {
                            //显示加载中界面
                            //getView().showDialog();
                        }
                    }
                })
                //请求服务
                .map(new Function<String, List<ImageEntity>>() {
                    @Override
                    public List<ImageEntity> apply(@NonNull String s) throws Exception {
                        return MediaUtil.getMediaImageList((Context) getView());
                    }
                })
                //在主线程接收数据
                .observeOn(AndroidSchedulers.mainThread())
                //访问后，关闭对话框等操作
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (isViewAttached()) {
                            //getView().hideDialog();
                        }
                    }
                })
                .subscribeWith(new DisposableObserver<List<ImageEntity>>() {

                    @Override
                    public void onNext(@NonNull List<ImageEntity> imageEntities) {
                        if (isViewAttached()) {
                            doAfterOnNext(imageEntities);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );

    }

    private void doAfterOnNext(@NonNull List<ImageEntity> imageEntities) {
        mAllImageList = imageEntities;
        mAllImageDirList = MediaUtil.getMediaImageListSortByDir(imageEntities);
        //把所有图片的数据，放置在第一个目录
        ImageDirEntity imageDirEntity = new ImageDirEntity();
        if (null != imageDirEntity) {
            imageDirEntity.setDir("");
            imageDirEntity.setPathList(mAllImageList);
            mAllImageDirList.add(0, imageDirEntity);
        }

        //设置默认选中的目录为所有图片目录
        mSelectedDirEntity = imageDirEntity;

        getView().refreshRecyclerView(imageEntities);
        getView().setImageCount(imageDirEntity.getPathList().size());
        getView().setDoneBtnText(mCurrentChooseCount, mMaxChooseCount);
    }

    public void clickLayoutBottom() {
        if (!isViewAttached()) {
            return;
        }

        getView().showPopupWindow(mAllImageDirList, mSelectedDirEntity);

        //改变按钮样式
    }

    public void onItemDirClick(ImageDirEntity entity, int position) {
        if (!isViewAttached()) {
            return;
        }

        mSelectedDirEntity = entity;
        getView().refreshRecyclerView(entity.getPathList());

        onCloseWindow();
    }

    private void onCloseWindow() {
        if (!isViewAttached()) {
            return;
        }

        if (null != mSelectedDirEntity) {
            String dirName = mSelectedDirEntity.getDir();
            if (TextUtils.isEmpty(dirName)) {
                dirName = mDefaultAllPictureDirName;
            } else {
                dirName = ChoosePhotoUtil.getDirName(dirName);
            }
            getView().setDirText(dirName);
            getView().setImageCount(mSelectedDirEntity.getPathList().size());
        }
    }

    @Override
    public void detachView() {
        if (null != mDisposable) {
            mDisposable.clear();
        }
        super.detachView();
    }

    public void onClickCheckBox(CheckBox checkBox, ImageEntity entity, int position) {
        if (null == entity) {
            return;
        }

        if (!isViewAttached()) {
            return;
        }

        //点击之前是否选中
        boolean isChecked = entity.isChecked();
        if (isChecked) {
            if (mSelectedList.contains(entity)) {
                mSelectedList.remove(entity);
                entity.setChecked(false);
            }
        } else {

            if (isLegalChooseCount()) {
                if (!mSelectedList.contains(entity)) {
                    mSelectedList.add(entity);
                    entity.setChecked(true);
                }
            } else {
                getView().showOverMaxChooseCountThoast();
            }
        }

        checkBox.setChecked(entity.isChecked());

        //重新设置选中数量
        mCurrentChooseCount = mSelectedList.size();
        getView().setDoneBtnText(mCurrentChooseCount, mMaxChooseCount);
    }


    /**
     * 判断是否是合法的选中数
     * @return
     */
    public boolean isLegalChooseCount() {
        return mCurrentChooseCount < mMaxChooseCount;
    }

    public void onClickDone() {
        if (!isViewAttached()) {
            return;
        }

        getView().startAddPhotoActivity(mSelectedList);
        getView().finishCurrentActivity();
    }

    public void onClickImage(ImageEntity entity, int position) {
        if (!isViewAttached()) {
            return;
        }

        getView().showDetailPhotoActivity(mSelectedDirEntity.getPathList(), position, mCurrentChooseCount, mMaxChooseCount, mSelectedList);
    }
}
