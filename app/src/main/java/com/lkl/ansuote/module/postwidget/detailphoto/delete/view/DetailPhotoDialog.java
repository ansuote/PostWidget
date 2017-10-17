package com.lkl.ansuote.module.postwidget.detailphoto.delete.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.lkl.ansuote.hdqlibrary.widget.dialog.base.BaseDialog;
import com.lkl.ansuote.hdqlibrary.widget.dialog.base.IBaseDialog;
import com.lkl.ansuote.module.postwidget.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by huangdongqiang on 16/10/2017.
 */
public class DetailPhotoDialog implements IBaseDialog {
    private BaseDialog mBaseDialog;
    private float mDensity = 1.0f;
    @Bind(R.id.btn_close)
    Button mCloseBtn;
    @Bind(R.id.btn_verify)
    Button mDeleteBtn;
    @Bind(R.id.text_content)
    TextView mContent;
    private OnDetialDetelePhotoListener mListener;

    public DetailPhotoDialog(Context context) {
        mDensity = ScreenUtils.getScreenDensity();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_base_normal, null, false);
        ButterKnife.bind(this, view);

        initView(context);

        mBaseDialog = new BaseDialog.Builder(context, view)
                .setGravity(Gravity.CENTER)
                .setWidthScale(0.95f)
                .create();
    }

    private void initView(Context context) {
        if (null != mCloseBtn) {
            mCloseBtn.setText(context.getString(R.string.all_close));
        }

        if (null != mDeleteBtn) {
            mDeleteBtn.setText(context.getString(R.string.all_delete));
        }

        if (null != mContent) {
            mContent.setText(context.getString(R.string.dialog_delete_photo));
        }
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

    @OnClick({R.id.btn_close, R.id.btn_verify})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                if (null != mListener) {
                    mListener.onClose();
                }
                dimiss();
                break;
            case R.id.btn_verify:
                if (null != mListener) {
                    mListener.onDelete();
                }
                dimiss();
                break;
        }
    }

    public void setOnDetialDetelePhotoListener(OnDetialDetelePhotoListener listener) {
        mListener = listener;
    }

    public interface OnDetialDetelePhotoListener{
        void onClose();
        void onDelete();
    }

    @Override
    public void onDestory() {
        if (null != mBaseDialog) {
            mBaseDialog.onDestory();
        }
    }


}
