package com.lkl.ansuote.module.postwidget.choosephoto.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lkl.ansuote.hdqlibrary.mvp.BaseMVPActivity;
import com.lkl.ansuote.hdqlibrary.util.ActivityUtil;
import com.lkl.ansuote.module.postwidget.R;
import com.lkl.ansuote.module.postwidget.addPhoto.view.AddPhotoActivity;
import com.lkl.ansuote.module.postwidget.base.Constants;
import com.lkl.ansuote.module.postwidget.base.entity.ImageDirEntity;
import com.lkl.ansuote.module.postwidget.base.entity.ImageEntity;
import com.lkl.ansuote.module.postwidget.choosephoto.ChoosePhotoPresenter;
import com.lkl.ansuote.module.postwidget.choosephoto.ChoosePhotoUtil;
import com.lkl.ansuote.module.postwidget.detailphoto.choose.view.DetailChoosePhotoActivity;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lkl.ansuote.module.postwidget.base.Constants.REQUEST_CODE_CHOOSE_PHOTO;


public class ChoosePhotoActivity extends BaseMVPActivity<IChoosePhotoView, ChoosePhotoPresenter> implements IChoosePhotoView {
    @Bind(R.id.text_dir)
    TextView mTextDir;
    @Bind(R.id.image_status)
    ImageView mImageStatus;
    @Bind(R.id.text_count)
    TextView mTextCount;
    @Bind(R.id.btn_title_right)
    Button mBtnTitleRight;
    private ChoosePhotoAdapter mChoosePhotoAdapter;
    private final int REQUEST_CODE_PERMISSION_STORAGE = 100;
    private final int REQUEST_CODE_SETTING = 200;
    private final int ROW_COUNT = 3;    //列数
    private ChoosePhotoPopupWindow mPopupWindow;
    private static final String TAG = "ChoosePhotoActivity";

    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.layout_bottom)
    RelativeLayout mBottomLayout;

    @Override
    protected ChoosePhotoPresenter createPresenter() {
        return new ChoosePhotoPresenter();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_choose_photo);
        ButterKnife.bind(this);

        initRecyclerView();
    }

    private void initRecyclerView() {
        if (null != mRecyclerView) {
            mChoosePhotoAdapter = new ChoosePhotoAdapter();
            if (null != mChoosePhotoAdapter) {
                mChoosePhotoAdapter.setOnChoosePhotoClickListener(new ChoosePhotoAdapter.OnChoosePhotoClickListener() {

                    @Override
                    public void onClickCheckBox(CheckBox checkBox, ImageEntity entity, int position) {
                        if (null != mPresenter) {
                            mPresenter.onClickCheckBox(checkBox, entity, position);
                        }
                    }

                    @Override
                    public void onClickImage(ImageEntity entity, int position) {
                        if (null != mPresenter) {
                            mPresenter.onClickImage(entity, position);
                        }
                    }
                });
                mRecyclerView.setAdapter(mChoosePhotoAdapter);
            }
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, ROW_COUNT));
        }
    }

    @Override
    public void refreshRecyclerView(List<ImageEntity> list) {
        if (null != mChoosePhotoAdapter) {
            mChoosePhotoAdapter.setData(list);
        }
    }

    @Override
    public void showPopupWindow(List<ImageDirEntity> list, ImageDirEntity selectDir) {
        if (null == mPopupWindow) {
            mPopupWindow = new ChoosePhotoPopupWindow(this);
            mPopupWindow.setAnimationStyle(R.style.PopupwindowAnimation);
            mPopupWindow.setOnPopupWindowClickListener(new ChoosePhotoPopupWindow.OnPopupWindowClickListener() {
                @Override
                public void onItemDirClick(ImageDirEntity entity, int position) {
                    if (null != mPresenter) {
                        mPresenter.onItemDirClick(entity, position);
                    }
                }

                @Override
                public void onCloseWindow() {

                }
            });
        }

        mPopupWindow.setData(list, selectDir);
        mPopupWindow.showAsDropDown(mBottomLayout, 0, 0);
    }

    @Override
    public void setImageCount(int count) {
        if (null != mTextCount) {
            mTextCount.setText(String.valueOf(count));
        }
    }

    @Override
    public void setDirText(String text) {
        if (null != mTextDir && null != text) {
            mTextDir.setText(text);
        }
    }

    @Override
    public void showDetailPhotoActivity(List<ImageEntity> allList,
                                        int position,
                                        int currentChooseCount,
                                        int maxChooseCount,
                                        List<ImageEntity> selectedList) {
        DetailChoosePhotoActivity.actionStart(this, allList, position, currentChooseCount, maxChooseCount, selectedList);
    }

    @Override
    public void setDoneBtnText(int currentCount, int maxCount) {
        if (null != mBtnTitleRight) {
            mBtnTitleRight.setText(getString(R.string.btn_choose_photo_done, currentCount, maxCount));
        }
    }

    @Override
    public void showOverMaxChooseCountThoast() {
        Toast.makeText(this, getString(R.string.over_max_choose_count), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showChoosePhotoToast() {
        Toast.makeText(this, getString(R.string.choose_photo_toast), Toast.LENGTH_LONG).show();
    }

    @Override
    public void checkStoragePermission() {
        AndPermission.with(this)
                .requestCode(REQUEST_CODE_PERMISSION_STORAGE)
                .permission(Permission.STORAGE)
                .callback(permissionListener)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                        AndPermission.rationaleDialog(ChoosePhotoActivity.this, rationale).show();
                    }
                })
                .start();
    }

    /**
     * 回调监听。
     */
    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            if (null == mPresenter) {
                return;
            }

            switch (requestCode) {
                case REQUEST_CODE_PERMISSION_STORAGE: {
                    if (AndPermission.hasPermission(ChoosePhotoActivity.this, Permission.STORAGE)) {
                        mPresenter.getMediaImageList();
                    } else {
                        ActivityUtil.startSettingsActivity(ChoosePhotoActivity.this);
                    }
                    break;
                }
                default:
                    break;
            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            switch (requestCode) {
                case REQUEST_CODE_PERMISSION_STORAGE: {
                    if (AndPermission.hasPermission(ChoosePhotoActivity.this, Permission.STORAGE)) {
                        mPresenter.getMediaImageList();
                    } else {
                        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
                        if (AndPermission.hasAlwaysDeniedPermission(ChoosePhotoActivity.this, deniedPermissions)) {
                            // 第一种：用默认的提示语。
                            AndPermission.defaultSettingDialog(ChoosePhotoActivity.this, REQUEST_CODE_SETTING).show();
                        } else {
                            ActivityUtil.startSettingsActivity(ChoosePhotoActivity.this);
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };


    @OnClick({R.id.btn_title_left, R.id.btn_title_right, R.id.layout_bottom})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_title_left:
                finishCurrentActivity();
                break;
            case R.id.btn_title_right:
                if (null != mPresenter) {
                    mPresenter.onClickDone();
                }
                break;
            case R.id.layout_bottom:
                if (null != mPresenter) {
                    mPresenter.clickLayoutBottom();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SETTING: {
                if (null != mPresenter) {
                    mPresenter.getMediaImageList();
                }
                break;
            }
            case REQUEST_CODE_CHOOSE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    if (null != data) {
                        boolean finish = data.getBooleanExtra(Constants.EXTRA_FINISH_ACTIVITY, false);
                        if (finish) {
                            finishCurrentActivity();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }


    @Override
    public String getAllPictureDirName() {
        return ChoosePhotoUtil.getDefaultAllImageDirName(this);
    }

    @Override
    public void startAddPhotoActivity(List<ImageEntity> selectedList) {
        AddPhotoActivity.actionStart(this, selectedList, Constants.REQUEST_CODE_ALBUM);
    }

    /**
     * 启动此 Activity
     * @param context
     */
    public static void actionStart(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, ChoosePhotoActivity.class);
            if (null != intent) {
                context.startActivity(intent);
            }
        }
    }

    /**
     * 启动此 Activity
     * @param context
     * @param maxChooseCount    最大选择的数量
     */
    public static void actionStart(Context context, int maxChooseCount) {
        if (null != context) {
            Intent intent = new Intent(context, ChoosePhotoActivity.class);
            if (null != intent) {
                intent.putExtra(Constants.EXTRA_PHOTO_MAX_CHOOSE_COUNT, maxChooseCount);
                context.startActivity(intent);
            }
        }
    }

    @Override
    public void finishCurrentActivity() {
        finish();
    }
}
