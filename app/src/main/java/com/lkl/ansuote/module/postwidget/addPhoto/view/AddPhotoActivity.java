package com.lkl.ansuote.module.postwidget.addPhoto.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lkl.ansuote.hdqlibrary.mvp.BaseMVPActivity;
import com.lkl.ansuote.hdqlibrary.util.Utils;
import com.lkl.ansuote.hdqlibrary.widget.dialog.base.BaseDialog;
import com.lkl.ansuote.module.postwidget.BaseApplication;
import com.lkl.ansuote.module.postwidget.R;
import com.lkl.ansuote.module.postwidget.addPhoto.AddPhotoPresenter;
import com.lkl.ansuote.module.postwidget.addPhoto.view.postaddimage.entity.PostAddImageEntity;
import com.lkl.ansuote.module.postwidget.addPhoto.view.postaddimage.view.OnPostAddImageClickListener;
import com.lkl.ansuote.module.postwidget.addPhoto.view.postaddimage.view.PostAddImageView;
import com.lkl.ansuote.module.postwidget.base.Constants;
import com.lkl.ansuote.module.postwidget.base.entity.ImageEntity;
import com.lkl.ansuote.module.postwidget.choosephoto.view.ChoosePhotoActivity;
import com.lkl.ansuote.module.postwidget.detailphoto.delete.view.DetailDetelePhotoActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddPhotoActivity extends BaseMVPActivity<IAddPhotoView, AddPhotoPresenter> implements IAddPhotoView {
    @Bind(R.id.post_add_image_view)
    PostAddImageView mPostAddImageView;
    @Bind(R.id.edit_text)
    EditText mEditText;

    private ChoosePhotoDialog mChoosePhotoDialog;
    private ExitDialog mExitDialog;

    @Override
    protected AddPhotoPresenter createPresenter() {
        return new AddPhotoPresenter();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_add_photo);
        ButterKnife.bind(this);
        initDialog(this);
        regEvent(true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (null != mPresenter) {
            mPresenter.onNewIntent(intent);
        }
    }

    private void regEvent(boolean b) {
        if (null != mPostAddImageView) {
            mPostAddImageView.setOnPostAddImageClickListener(b ? new OnPostAddImageClickListener() {
                @Override
                public void onClickImage(int position, PostAddImageEntity entity) {
                    if (null != mPresenter) {
                        mPresenter.clickImage(position, entity);
                    }
                }

                @Override
                public void onClickAddBtn() {
                    showChoosePhotoDialog();
                }
            } : null);
        }
    }

    @Override
    public void refresh(List<PostAddImageEntity> list) {
        if (null != mPostAddImageView) {
            mPostAddImageView.refresh(list);
        }
    }

    @Override
    public int getCurrentChooseCount() {
        if (null != mPostAddImageView) {
            return mPostAddImageView.getCurrentChooseCount();
        }
        return 0;
    }

    @Override
    public void showChoosePhotoDialog() {
        if (null != mChoosePhotoDialog) {
            mChoosePhotoDialog.show();
        }
    }

    @Override
    public void startChoosePhotoActivity(int maxChooseCount) {
        ChoosePhotoActivity.actionStart(this, maxChooseCount);
    }

    private void initDialog(final Context context) {
        mChoosePhotoDialog = new ChoosePhotoDialog(this);
        mChoosePhotoDialog.setOnChoosePhotoDialogClickListener(new ChoosePhotoDialog.OnChoosePhotoDialogClickListener() {
            @Override
            public void onClickTakePhotoListener(BaseDialog dialog) {
                if (null != mPresenter) {
                    mPresenter.onClickTakePhotoListener(AddPhotoActivity.this);
                }
            }

            @Override
            public void onClickChoosePhotoFromAlbumListener(BaseDialog dialog) {
                if (null != mPresenter) {
                    mPresenter.onClickChoosePhotoFromAlbumListener();
                }
            }

            @Override
            public void onClickCancelListener(BaseDialog dialog) {

            }
        });
    }

    @Override
    public void startDetailDeletePhotoActivity(List<ImageEntity> allList, int position) {
        DetailDetelePhotoActivity.actionStart(this, allList, position);
    }

    /**
     * 启动此 Activity
     *
     * @param context
     * @param list
     * @param type    从哪个界面启动 Constants.REQUEST_CODE_ALBUM , Constants.REQUEST_CODE_CAMERA
     */
    public static void actionStart(Context context, List<ImageEntity> list, int type) {
        if (null == context) {
            return;
        }

        Intent intent = new Intent(context, AddPhotoActivity.class);
        if (null != intent) {
            //intent.putParcelableArrayListExtra(Constants.EXTRA_PHOTO_SELECT_LIST, (ArrayList<? extends Parcelable>) list);
            Utils.writeObjToFile(list, Utils.getFilePath(BaseApplication.getInstance(), Constants.PostWidget.FILE_NAME_PHOTO_SELECT_LIST));
            intent.putExtra(Constants.EXTRA_REQUEST_CODE, type);
            context.startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != mPresenter) {
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        regEvent(false);
        super.onDestroy();
    }

    @OnClick({R.id.btn_left, R.id.btn_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                showEixtDialog();
                break;
            case R.id.btn_right:
                if (null != mPresenter) {
                    mPresenter.clickDone();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void showMustChoosePhotoToast() {
        Toast.makeText(this, getString(R.string.choose_photo_toast), Toast.LENGTH_LONG).show();
    }

    @Override
    public String getEidtText() {
        if (null != mEditText && null != mEditText.getText()) {
            return mEditText.getText().toString();
        }
        return null;
    }

    @Override
    public void showEixtDialog() {
        if (null == mExitDialog) {
            mExitDialog = new ExitDialog(this);
            mExitDialog.setOnExitDialogListener(new ExitDialog.OnExitDialogListener() {
                @Override
                public void onClose() {

                }

                @Override
                public void onVerify() {
                    finish();
                }
            });
        }

        if (null != mExitDialog) {
            mExitDialog.show();
        }
    }
}
