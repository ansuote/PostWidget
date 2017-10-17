package com.lkl.ansuote.module.postwidget.addPhoto.view;

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
public class ExitDialog implements IBaseDialog {
    private BaseDialog mBaseDialog;
    private float mDensity = 1.0f;
    @Bind(R.id.btn_close)
    Button mCloseBtn;
    @Bind(R.id.btn_verify)
    Button mVerifyBtn;
    @Bind(R.id.text_content)
    TextView mContent;
    private OnExitDialogListener mOnExitDialogListener;

    public ExitDialog(Context context) {
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
            mCloseBtn.setText(context.getString(R.string.all_cancel));
        }

        if (null != mVerifyBtn) {
            mVerifyBtn.setText(context.getString(R.string.all_done));
        }

        if (null != mContent) {
            mContent.setText(context.getString(R.string.dialog_exit));
        }
    }

    public void setOnExitDialogListener(OnExitDialogListener listener) {
        mOnExitDialogListener = listener;
    }

    public interface OnExitDialogListener{
        void onClose();
        void onVerify();
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
                if (null != mOnExitDialogListener) {
                    mOnExitDialogListener.onClose();
                }
                dimiss();
                break;
            case R.id.btn_verify:
                if (null != mOnExitDialogListener) {
                    mOnExitDialogListener.onVerify();
                }
                dimiss();
                break;
        }
    }

    @Override
    public void onDestory() {
        if (null != mBaseDialog) {
            mBaseDialog.onDestory();
        }
    }
}
