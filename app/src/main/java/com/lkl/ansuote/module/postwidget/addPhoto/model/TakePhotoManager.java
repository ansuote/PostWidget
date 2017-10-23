package com.lkl.ansuote.module.postwidget.addPhoto.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.lkl.ansuote.hdqlibrary.util.ActivityUtil;
import com.lkl.ansuote.hdqlibrary.util.Utils;
import com.lkl.ansuote.module.postwidget.R;
import com.lkl.ansuote.module.postwidget.base.Constants;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.io.File;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * 拍照类
 * Created by huangdongqiang on 16/10/2017.
 */
public class TakePhotoManager {
    private Activity mContext;
    private File mTempFile;
    private boolean mClipPhotoEnable = true;   //是否剪辑图片
    private static final int DEFAULT_MAX_PHOTO_SIZE = 200000;
    private int mMaxPhotoSize = DEFAULT_MAX_PHOTO_SIZE;  //最大压缩的图片大小
    private OnTakePhotoListener mOnTakePhotoListener;

    public TakePhotoManager(Activity context) {
        this(context, true, DEFAULT_MAX_PHOTO_SIZE);
    }

    public TakePhotoManager(Activity context, boolean clipPhotoEnable, int maxPhotoSize) {
        mContext = context;
        mClipPhotoEnable = clipPhotoEnable;
        mMaxPhotoSize = maxPhotoSize;
        mTempFile = new File(mContext.getExternalCacheDir(), "addAlbum.jpg");
    }

    public void takePhoto(OnTakePhotoListener listener) {
        if (null == mContext) {
            return;
        }
        mOnTakePhotoListener = listener;

        checkCameraPermission(mContext);

    }

    private void checkCameraPermission(final Activity context) {
        AndPermission.with(mContext)
                .requestCode(Constants.Permission.REQUEST_CODE_CAMERA)
                .permission(Permission.CAMERA)
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        switch (requestCode) {
                            case Constants.Permission.REQUEST_CODE_CAMERA: {
                                if (AndPermission.hasPermission(context, Permission.CAMERA)) {
                                    doTakePhoto();
                                } else {
                                    Toast.makeText(context, context.getString(R.string.permission_camera_turn_on_toast), Toast.LENGTH_LONG).show();
                                    ActivityUtil.startSettingsActivity(context);
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
                            case Constants.Permission.REQUEST_CODE_CAMERA: {
                                if (AndPermission.hasPermission(context, Permission.CAMERA)) {
                                    doTakePhoto();
                                } else {
                                    // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
                                    if (AndPermission.hasAlwaysDeniedPermission(context, deniedPermissions)) {
                                        // 第一种：用默认的提示语。
                                        AndPermission.defaultSettingDialog(context, Constants.REQUEST_CODE_SETTING).show();
                                        //AndPermission.defaultSettingDialog(context).show();
                                    } else {
                                        ActivityUtil.startSettingsActivity(context);
                                    }
                                }
                                break;
                            }
                            default:
                                break;
                        }
                    }
                })
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                        AndPermission.rationaleDialog(context, rationale).show();
                    }
                })
                .start();
    }

    private void doTakePhoto() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.Images.ImageColumns.ORIENTATION, 0);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTempFile));
            mContext.startActivityForResult(intent, Constants.REQUEST_CODE_CAMERA);
        } else {
            Toast.makeText(mContext, R.string.out_storage_unused, Toast.LENGTH_LONG).show();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null == mContext) {
            return;
        }

        if (requestCode == Constants.REQUEST_CODE_CAMERA) {
            if (null == mOnTakePhotoListener) {
                return;
            }

            if (resultCode == Activity.RESULT_OK) {
                if (mClipPhotoEnable) {
                    doCropPhoto();
                } else {
                    mOnTakePhotoListener.onTakePhotoSuccess(getCompressPhoto(mContext, mTempFile.getPath(), mMaxPhotoSize));
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {

                mOnTakePhotoListener.onTakePhotoCancel();
            } else {

            }

        } else if (requestCode == Constants.REQUEST_CODE_CROP) {
            if (null == mOnTakePhotoListener) {
                return;
            }

            //不管剪裁成功与否，都可以走下面流程

            /*if (resultCode == Activity.RESULT_OK) {
                mOnTakePhotoListener.onTakePhotoSuccess(getCompressPhoto(mContext, mTempFile.getPath(), mMaxPhotoSize));
            } else if (resultCode == Activity.RESULT_CANCELED) {
                mOnTakePhotoListener.onTakePhotoCancel();
            } else {

            }*/

            mOnTakePhotoListener.onTakePhotoSuccess(getCompressPhoto(mContext, mTempFile.getPath(), mMaxPhotoSize));
        }
    }

    /**
     * 获取压缩后的图片大小
     * @param context
     * @param originalPath  原来图片路径
     * @param maxPhotoSize  最大压缩大小
     * @return
     */
    private  String getCompressPhoto(Context context, String originalPath, int maxPhotoSize) {
        if (null != context) {
            String imagePath = context.getExternalCacheDir() + "/" + System.currentTimeMillis() + ".jpg";
            Bitmap map       = Utils.revitionImageSize(originalPath, maxPhotoSize);
            Utils.saveBitmap(map, imagePath);
            return imagePath;
        }

        return null;
    }

    /**
     * 剪辑图片
     */
    private void doCropPhoto() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(mTempFile), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTempFile));
        //intent.putExtra("outputFormat", "jpg");
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        mContext.startActivityForResult(intent, Constants.REQUEST_CODE_CROP);
    }

    public interface OnTakePhotoListener {
        void onTakePhotoSuccess(String path);
        void onTakePhotoCancel();
    }

}