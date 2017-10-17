package com.lkl.ansuote.module.postwidget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lkl.ansuote.hdqlibrary.widget.dialog.base.BaseDialog;
import com.lkl.ansuote.module.postwidget.addPhoto.model.TakePhotoManager;
import com.lkl.ansuote.module.postwidget.addPhoto.view.AddPhotoActivity;
import com.lkl.ansuote.module.postwidget.addPhoto.view.ChoosePhotoDialog;
import com.lkl.ansuote.module.postwidget.base.Constants;
import com.lkl.ansuote.module.postwidget.base.entity.ImageEntity;
import com.lkl.ansuote.module.postwidget.choosephoto.view.ChoosePhotoActivity;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ChoosePhotoDialog mDialog;
    private TakePhotoManager mTakePhotoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDialog(this);

        mTakePhotoManager = new TakePhotoManager(this);

        findViewById(R.id.btn_open_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
            }
        });
    }

    private void initDialog(final Context context) {
        mDialog = new ChoosePhotoDialog(this);
        mDialog.setOnChoosePhotoDialogClickListener(new ChoosePhotoDialog.OnChoosePhotoDialogClickListener() {
            @Override
            public void onClickTakePhotoListener(BaseDialog dialog) {
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
                            AddPhotoActivity.actionStart(MainActivity.this, list, Constants.REQUEST_CODE_CAMERA);
                        }
                    }

                    @Override
                    public void onTakePhotoCancel() {

                    }
                });
            }

            @Override
            public void onClickChoosePhotoFromAlbumListener(BaseDialog dialog) {
                ChoosePhotoActivity.actionStart(context);
            }

            @Override
            public void onClickCancelListener(BaseDialog dialog) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != mTakePhotoManager) {
            mTakePhotoManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}
