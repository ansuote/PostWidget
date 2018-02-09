package com.lkl.ansuote.module.postwidget.addPhoto.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.blankj.utilcode.util.ScreenUtils;
import com.lkl.ansuote.hdqlibrary.widget.dialog.base.BaseDialog;
import com.lkl.ansuote.hdqlibrary.widget.dialog.base.IBaseDialog;
import com.lkl.ansuote.module.postwidget.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择图片时候，弹出的对话框，有选项：拍照/从相册选择/取消
 * Created by huangdongqiang on 13/10/2017.
 */
public class ChoosePhotoDialog implements IBaseDialog {
    private BaseDialog mBaseDialog;
    private float mDensity = 1.0f;
    private OnChoosePhotoDialogClickListener mListener;

    public ChoosePhotoDialog(Context context) {
        mDensity = ScreenUtils.getScreenDensity();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_choose_photo, null, false);
        ButterKnife.bind(this, view);

        mBaseDialog = new BaseDialog.Builder(context, view)
                .setGravity(Gravity.BOTTOM)
                .setMarginBottom((int) (mDensity * 16))
                .setWidthScale(0.95f)
                .create();
    }


    @OnClick({R.id.btn_take_photo, R.id.btn_choose_photo_from_album, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_take_photo:
                if (null != mListener) {
                    mListener.onClickTakePhotoListener(mBaseDialog);
                }
                mBaseDialog.dimiss();
                break;
            case R.id.btn_choose_photo_from_album:
                if (null != mListener) {
                    mListener.onClickChoosePhotoFromAlbumListener(mBaseDialog);
                }
                mBaseDialog.dimiss();
                break;
            case R.id.btn_cancel:
                if (null != mListener) {
                    mListener.onClickCancelListener(mBaseDialog);
                }
                mBaseDialog.dimiss();
                break;
            default:
                break;
        }
    }

    public void setOnChoosePhotoDialogClickListener(OnChoosePhotoDialogClickListener listener) {
        mListener = listener;
    }

    public interface OnChoosePhotoDialogClickListener {
        void onClickTakePhotoListener(BaseDialog dialog);
        void onClickChoosePhotoFromAlbumListener(BaseDialog dialog);
        void onClickCancelListener(BaseDialog dialog);
    }

    @Override
    public void show() {
        if (null != mBaseDialog) {
            mBaseDialog.show();
        }
    }

    @Override
    public void dimiss() {
        if (null != mBaseDialog) {
            mBaseDialog.dimiss();
        }
    }

    @Override
    public void onDestory() {
        if (null != mBaseDialog) {
            mBaseDialog.onDestory();
        }
    }
}
