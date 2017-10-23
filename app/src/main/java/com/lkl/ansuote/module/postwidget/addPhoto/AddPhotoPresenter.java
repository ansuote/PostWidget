package com.lkl.ansuote.module.postwidget.addPhoto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.lkl.ansuote.hdqlibrary.mvp.BasePresenter;
import com.lkl.ansuote.hdqlibrary.util.Utils;
import com.lkl.ansuote.module.postwidget.BaseApplication;
import com.lkl.ansuote.module.postwidget.addPhoto.model.TakePhotoManager;
import com.lkl.ansuote.module.postwidget.addPhoto.view.AddPhotoActivity;
import com.lkl.ansuote.module.postwidget.addPhoto.view.IAddPhotoView;
import com.lkl.ansuote.module.postwidget.addPhoto.view.postaddimage.entity.PostAddImageEntity;
import com.lkl.ansuote.module.postwidget.base.Constants;
import com.lkl.ansuote.module.postwidget.base.entity.ImageEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangdongqiang on 13/10/2017.
 */
public class AddPhotoPresenter extends BasePresenter<IAddPhotoView> {
    private static final int MAX_CHOOSE_COUNT = 9;  //最大的选择数
    private int mMaxChooseCount = MAX_CHOOSE_COUNT;    //最大选择的数量
    private List<PostAddImageEntity> mList; //总数据集
    private List<PostAddImageEntity> mCurrentImageList; //上个界面传过来的数据集
    private int mRequestCode;   //标识是从那个界面启动的，作为预留字段
    private TakePhotoManager mTakePhotoManager;

    @Override
    public void initVariables(Bundle savedInstanceState, Intent intent) {
        if (null != intent) {
            //List<ImageEntity> list = intent.getParcelableArrayListExtra(Constants.EXTRA_PHOTO_SELECT_LIST);
            List<ImageEntity> list = (List<ImageEntity>)Utils.readObjFromFile(Utils.getFilePath(BaseApplication.getInstance(), Constants.PostWidget.FILE_NAME_PHOTO_SELECT_LIST));

            mCurrentImageList = changeToPostAddIamgeList(list);
            mList = getFinalList(mCurrentImageList);
        }
    }

    @Override
    public void onStart() {
        if (!isViewAttached()) {
            return;
        }

        getView().refresh(mList);
    }

    /**
     * 类型转换，转换适用于拖拽的 PostAddImageEntity 类型
     * @param list
     * @return
     */
    private List<PostAddImageEntity> changeToPostAddIamgeList(List<ImageEntity> list) {
        ArrayList<PostAddImageEntity> resultList = new ArrayList<>();
        if (null != resultList && null != list) {
            for (int i = 0; i < list.size(); i++) {
                ImageEntity entity = list.get(i);
                if (null != entity) {
                    String url = entity.getPath();
                    if (!TextUtils.isEmpty(url)) {
                        PostAddImageEntity resultEntity = new PostAddImageEntity(PostAddImageEntity.TYPE_IMAGE);
                        //拼接 file://
                        resultEntity.setUrl(Constants.PRE_FILE + url);
                        resultList.add(resultEntity);
                    }
                }
            }
        }
        return resultList;
    }

    /**
     * 把当前RecyclerView 的数据集类型 PostAddImageEntity 转换为 ImageEntity , 只保留图片部分
     * @param list
     * @return
     */
    private List<ImageEntity> chageToImageEntityList(List<PostAddImageEntity> list) {
        ArrayList<ImageEntity> resultList = new ArrayList<>();
        if (null != resultList && null != list) {
            for (PostAddImageEntity entity : list) {
                if (entity.getItemType() == PostAddImageEntity.TYPE_IMAGE) {
                    ImageEntity resultEntity = new ImageEntity();
                    if (null != resultEntity) {
                        String url = entity.getUrl();
                        //去除前缀，还原原本路径
                        url = url.replace(Constants.PRE_FILE, "");
                        resultEntity.setPath(url);
                        resultList.add(resultEntity);
                    }
                }
            }
        }
        return resultList;
    }

    /**
     * 通过判断个数，决定是否拼接最后的 加号按钮
     * @return
     */
    private List<PostAddImageEntity> getFinalList(List<PostAddImageEntity> list) {
        List<PostAddImageEntity> allList = new ArrayList<>();
        if (null != allList && null != list) {
            allList.addAll(list);

            if (list.size() < MAX_CHOOSE_COUNT) {
                PostAddImageEntity entity = new PostAddImageEntity(PostAddImageEntity.TYPE_BUTTON_ADD);
                allList.add(entity);
            }
        }

        return allList;
    }

    public void clickImage(int position, PostAddImageEntity entity) {
        if (!isViewAttached()) {
            return;
        }

        getView().startDetailDeletePhotoActivity(chageToImageEntityList(mList), position);
    }

    public void onClickChoosePhotoFromAlbumListener() {
        if (!isViewAttached()) {
            return;
        }

        getView().startChoosePhotoActivity(mMaxChooseCount - getView().getCurrentChooseCount());
    }

    public void onNewIntent(Intent intent) {
        if (null != intent) {
            //List<ImageEntity> list = intent.getParcelableArrayListExtra(Constants.EXTRA_PHOTO_SELECT_LIST);
            List<ImageEntity> list = (List<ImageEntity>)Utils.readObjFromFile(Utils.getFilePath(BaseApplication.getInstance(), Constants.PostWidget.FILE_NAME_PHOTO_SELECT_LIST));

            mRequestCode = intent.getIntExtra(Constants.EXTRA_REQUEST_CODE, Constants.REQUEST_CODE_ALBUM);

            //如果是删除图片界面回来的，则清楚原本数据
            if (mRequestCode == Constants.REQUEST_CODE_DELETE_PHOTO) {
                mCurrentImageList.clear();
            }

            //Constants.REQUEST_CODE_ALBUM  /  Constants.REQUEST_CODE_CAMERA 不做区分，统一执行下面逻辑
            List<PostAddImageEntity> oneChooseList = changeToPostAddIamgeList(list);
            if (null != oneChooseList && null != mCurrentImageList) {
                mCurrentImageList.addAll(oneChooseList);
                mList = getFinalList(mCurrentImageList);
                getView().refresh(mList);
            }
        }
    }

    public void onClickTakePhotoListener(final Activity context) {
        if (null == mTakePhotoManager && null != context) {
            mTakePhotoManager = new TakePhotoManager(context);
        }

        if (null != mTakePhotoManager) {
            mTakePhotoManager.takePhoto(new TakePhotoManager.OnTakePhotoListener() {
                @Override
                public void onTakePhotoSuccess(String path) {
                    ArrayList<ImageEntity> list = new ArrayList<ImageEntity>();
                    if (null != list) {
                        ImageEntity entity = new ImageEntity();
                        if (null != entity) {
                            entity.setPath(path);
                            list.add(entity);
                        }
                        AddPhotoActivity.actionStart(context, list, Constants.REQUEST_CODE_CAMERA);
                    }
                }

                @Override
                public void onTakePhotoCancel() {

                }
            });
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != mTakePhotoManager) {
            mTakePhotoManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void clickDone() {
        if (!isViewAttached()) {
            return;
        }

        List<ImageEntity> imageEntities = chageToImageEntityList(mList);
        if (null != imageEntities && imageEntities.size() <= 0) {
            getView().showMustChoosePhotoToast();
            return;
        }

        String text = getView().getEidtText();
    }
}
